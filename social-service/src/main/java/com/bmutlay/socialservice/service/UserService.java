package com.bmutlay.socialservice.service;

import com.bmutlay.socialservice.model.PostCreatedEvent;
import com.bmutlay.socialservice.model.User;
import com.bmutlay.socialservice.model.UserFollowEvent;
import com.bmutlay.socialservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import payload.ApiResponse;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final KafkaTemplate<UUID, PostCreatedEvent> postCreatedEventKafkaTemplate;
    private final KafkaTemplate<UUID, UserFollowEvent> userFollowEventKafkaTemplate;

    public User createUser(User user) {
        Set<User> users = new HashSet<>();
        user.setFollowing(users);
        return userRepository.save(user);
    }

    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }

    @Transactional
    public ApiResponse follow(String follower, String followed) {
        User followerUser = userRepository
                .findByUsername(follower)
                .orElseThrow(() -> new NoSuchElementException("{} user could not be found"));

        User followedUser = userRepository
                .findByUsername(followed)
                .orElseThrow(() -> new NoSuchElementException("{} user could not be found"));
        followerUser.getFollowing().add(followedUser);

        String responseMessage = String.format("user %s started following %s", follower, followed);

        userRepository.save(followerUser);
        sendUserFollowEvent(UUID.randomUUID(), new UserFollowEvent(follower, followed, UserFollowEvent.Status.FOLLOWED));
        return new ApiResponse(responseMessage);
    }

    @Transactional
    public ApiResponse unfollow(String follower, String followed) {

        userRepository.unfollow(follower, followed);
        sendUserFollowEvent(UUID.randomUUID(), new UserFollowEvent(follower, followed, UserFollowEvent.Status.UNFOLLOWED));

        String responseMessage = String.format("user %s is no longer following %s", follower, followed);

        return new ApiResponse(responseMessage);
    }

    public List<String> getFollowers(String username) {
        userRepository.findByUsername(username).orElseThrow(() -> new NoSuchElementException("User not found:" + username));

        return userRepository.findFollowers(username).stream().map(User::getUsername).collect(Collectors.toList());
    }

    public List<String> getFollowings(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NoSuchElementException("User not found:" + username));
        return user.getFollowing().stream().map(User::getUsername).collect(Collectors.toList());
    }

    public void populatePostCreatedEventAndSend(PostCreatedEvent postCreatedEvent) {
        postCreatedEvent.setFollowers(new HashSet<>(this.getFollowers(postCreatedEvent.getCreatedBy())));
        sendPostCreatedEvent("post-follower-topic", UUID.randomUUID(), postCreatedEvent);
    }

    public void sendPostCreatedEvent(String topicName, UUID key, PostCreatedEvent postCreatedEvent) {
        var future = postCreatedEventKafkaTemplate.send(topicName, key, postCreatedEvent);
        future.whenComplete((sendResult, exception) -> {
            if (exception != null) {
                log.error(exception.getMessage());
                future.completeExceptionally(exception);
            } else {
                future.complete(sendResult);
            }
            log.info("PostCreatedEvent with transaction id: {} published for Post ID: {} with follower size: {}",
                    postCreatedEvent.getTransactionId(),
                    postCreatedEvent.getPostId(),
                    postCreatedEvent.getFollowers().size());
        });
    }

    public void sendUserFollowEvent(UUID key, UserFollowEvent userFollowEvent) {
        var future = userFollowEventKafkaTemplate.send("follow-event-topic", key, userFollowEvent);
        future.whenComplete((sendResult, exception) -> {
            if (exception != null) {
                log.error(exception.getMessage());
                future.completeExceptionally(exception);
            } else {
                future.complete(sendResult);
            }
            log.info("UserFollowEvent with transaction id: {} published for follower: {} with followed: {}, status: {}",
                    userFollowEvent.getTransactionId(),
                    userFollowEvent.getFollower(),
                    userFollowEvent.getFollowed(),
                    userFollowEvent.getStatus().toString());
        });
    }

}
