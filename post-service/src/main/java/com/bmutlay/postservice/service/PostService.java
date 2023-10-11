package com.bmutlay.postservice.service;

import com.bmutlay.postservice.model.Post;
import com.bmutlay.postservice.repository.PostRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findPostsByUsername(String username) {
        return postRepository.findByUsernameOrderByCreatedAtDesc(username);
    }

    public Post createPost(Post post) {
        return postRepository.insert(post);
    }

    public List<Post> getPostsByUser(String username, Pageable pageable) {
        return postRepository.findAllByUsername(username, pageable);
    }

    public String deletePost(String id) {
        postRepository.deleteById(id);
        return id;
    }
}
