package com.bmutlay.socialservice.service;

import com.bmutlay.socialservice.model.User;
import com.bmutlay.socialservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import payload.ApiResponse;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

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

        return new ApiResponse(responseMessage);
    }

    @Transactional
    public ApiResponse unfollow(String follower, String followed) {

        userRepository.unfollow(follower, followed);

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

}
