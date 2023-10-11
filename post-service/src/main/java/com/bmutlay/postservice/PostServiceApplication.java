package com.bmutlay.postservice;

import com.bmutlay.postservice.model.Post;
import com.bmutlay.postservice.repository.PostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.time.Instant;

@SpringBootApplication
@EnableMongoRepositories
@EnableMongoAuditing
public class PostServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostServiceApplication.class, args);
	}

	/*@Bean
	CommandLineRunner runner(PostRepository repository) {
		return args -> {
			Post post = new Post();
			post.setText("Some text");
			post.setUsername("bmutluy");
			post.setCreatedAt(Instant.now());
			post.setImageUrl("some url");
			post.setUpdatedAt(Instant.now());
			repository.insert(post);
		};
	}
*/
}
