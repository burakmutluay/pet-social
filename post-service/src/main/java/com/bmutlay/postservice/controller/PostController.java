package com.bmutlay.postservice.controller;

import com.bmutlay.postservice.model.Post;
import com.bmutlay.postservice.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/post")
public class PostController {
    @Autowired
    private PostService postService;
    @PostMapping("/create")
    public ResponseEntity<Post> addPost(@RequestBody Post post) {
        return new ResponseEntity<>(postService.createPost(post), HttpStatus.OK);
    }
}
