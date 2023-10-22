package com.bmutlay.postservice.repository;

import com.bmutlay.postservice.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {

    List<Post> findByUsernameOrderByCreatedAtDesc(String username);

    List<Post> findAllByUsername(String username, Pageable pageable);

    List<Post> findPostsByCreatedAtAfterAndUsername(Instant instant, String username);

}
