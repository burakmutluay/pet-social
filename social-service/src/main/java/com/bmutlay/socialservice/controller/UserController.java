package com.bmutlay.socialservice.controller;

import com.bmutlay.socialservice.model.User;
import com.bmutlay.socialservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import payload.ApiResponse;
import payload.FollowRequest;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody User user){
        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity<List<String>> getFollowers(@PathVariable String username) {
        return ResponseEntity.ok(userService.getFollowers(username));
    }

    @GetMapping("/{username}/following")
    public ResponseEntity<List<String>> getFollowing(@PathVariable String username) {
        return ResponseEntity.ok(userService.getFollowings(username));
    }

    @PostMapping("/follow")
    public ResponseEntity<ApiResponse> follow(@RequestBody FollowRequest followRequest) {
        return ResponseEntity.ok(userService.follow(followRequest.getFollower(), followRequest.getFollowed()));
    }

    @PostMapping("/unfollow")
    public ResponseEntity<ApiResponse> unfollow(@RequestBody FollowRequest followRequest) {
        return ResponseEntity.ok(userService.unfollow(followRequest.getFollower(), followRequest.getFollowed()));
    }

}
