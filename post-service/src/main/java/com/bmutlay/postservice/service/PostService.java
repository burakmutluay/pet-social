package com.bmutlay.postservice.service;

import com.bmutlay.postservice.model.Post;
import com.bmutlay.postservice.model.PostCreatedEvent;
import com.bmutlay.postservice.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PostService {
    private final PostRepository postRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(PostService.class);

    private final KafkaTemplate<UUID, PostCreatedEvent> kafkaTemplate;

    public PostService(PostRepository postRepository, KafkaTemplate<UUID, PostCreatedEvent> kafkaTemplate) {
        this.postRepository = postRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public List<Post> findPostsByUsername(String username) {
        return postRepository.findByUsernameOrderByCreatedAtDesc(username);
    }

    public Post createPost(Post post) {
        Post createdPost = postRepository.insert(post);
        PostCreatedEvent postCreatedEvent = new PostCreatedEvent(createdPost.getId(), createdPost.getUsername());
        send("post-topic", UUID.randomUUID(), postCreatedEvent);
        return createdPost;
    }

    public List<Post> getPostsByUser(String username, Pageable pageable) {
        return postRepository.findAllByUsername(username, pageable);
    }

    public String deletePost(String id) {
        postRepository.deleteById(id);
        return id;
    }

    public void send(String topicName, UUID key, PostCreatedEvent postCreatedEvent) {
        var future = kafkaTemplate.send(topicName, key, postCreatedEvent);
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
}
