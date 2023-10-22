package com.bmutlay.postservice.service;

import com.bmutlay.postservice.model.Post;
import com.bmutlay.postservice.model.PostCreatedEvent;
import com.bmutlay.postservice.model.UserFollowEvent;
import com.bmutlay.postservice.model.dto.PostDTO;
import com.bmutlay.postservice.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(PostService.class);

    private final KafkaTemplate<UUID, PostCreatedEvent> postCreatedEventKafkaTemplate;

    private final KafkaTemplate<UUID, UserFollowEvent> userFollowEventKafkaTemplate;

    public PostService(PostRepository postRepository, KafkaTemplate<UUID, PostCreatedEvent> kafkaTemplate, KafkaTemplate<UUID, UserFollowEvent> userFollowEventKafkaTemplate) {
        this.postRepository = postRepository;
        this.postCreatedEventKafkaTemplate = kafkaTemplate;
        this.userFollowEventKafkaTemplate = userFollowEventKafkaTemplate;
    }

    public List<Post> findPostsByUsername(String username) {
        return postRepository.findByUsernameOrderByCreatedAtDesc(username);
    }

    public Post createPost(Post post) {
        Post createdPost = postRepository.insert(post);
        PostCreatedEvent postCreatedEvent = new PostCreatedEvent(createdPost.getId(), createdPost.getUsername());
        sendPostCreatedEvent("post-topic", UUID.randomUUID(), postCreatedEvent);
        return createdPost;
    }

    public List<Post> getPostsByUser(String username, Pageable pageable) {
        return postRepository.findAllByUsername(username, pageable);
    }

    public String deletePost(String id) {
        postRepository.deleteById(id);
        return id;
    }

    public void populateAndPublishUserFollowEvent(UserFollowEvent userFollowEvent){
        userFollowEvent.setPosts(findLatestPostsByUsername(userFollowEvent.getFollowed()));
        sendUserFollowEvent(UUID.randomUUID(), userFollowEvent);
    }

    public List<PostDTO> findLatestPostsByUsername(String username) {
        Instant sevenDaysBefore = Instant.now().minus(7, ChronoUnit.DAYS);
        List<Post> posts = postRepository.findPostsByCreatedAtAfterAndUsername(sevenDaysBefore, username);
        return posts.stream().map(post -> new PostDTO(post.getId(), post.getCreatedAt())).collect(Collectors.toList());
    }

    public void sendPostCreatedEvent(String topicName, UUID key, PostCreatedEvent postCreatedEvent) {
        var future = postCreatedEventKafkaTemplate.send(topicName, key, postCreatedEvent);
        future.whenComplete((sendResult, exception) -> {
            if (exception != null) {
                LOGGER.error(exception.getMessage());
                future.completeExceptionally(exception);
            } else {
                future.complete(sendResult);
            }
            LOGGER.info("PostCreatedEvent with transaction id: {} published for Post ID: {}",
                    postCreatedEvent.getTransactionId(),
                    postCreatedEvent.getPostId());
        });
    }

    public void sendUserFollowEvent(UUID key, UserFollowEvent userFollowEvent) {
        var future = userFollowEventKafkaTemplate.send("post-feed-follow-topic", key, userFollowEvent);
        future.whenComplete((sendResult, exception) -> {
            if (exception != null) {
                LOGGER.error(exception.getMessage());
                future.completeExceptionally(exception);
            } else {
                future.complete(sendResult);
            }
            LOGGER.info("UserFollowEvent with transaction id: {} published for follower: {} followed: {}. PostList size: {}",
                    userFollowEvent.getTransactionId(),
                    userFollowEvent.getFollower(),
                    userFollowEvent.getFollowed(),
                    userFollowEvent.getPosts().size());
        });
    }
}
