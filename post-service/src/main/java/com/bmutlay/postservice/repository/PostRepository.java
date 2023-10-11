package com.bmutlay.postservice.repository;

import com.bmutlay.postservice.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {

    List<Post> findByUsernameOrderByCreatedAtDesc(String username);

    List<Post> findAllByUsername(String username, Pageable pageable);

}
