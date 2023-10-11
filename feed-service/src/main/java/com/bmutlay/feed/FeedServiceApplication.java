package com.bmutlay.feed;

import com.bmutlay.feed.model.Feed;
import com.bmutlay.feed.model.PostDTO;
import com.bmutlay.feed.repository.FeedRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.cassandra.config.EnableCassandraAuditing;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.time.Instant;
import java.util.List;

@SpringBootApplication
@EnableCassandraAuditing
public class FeedServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner clr(FeedRepository repository) {
		return args -> {
			repository.deleteAll();


			PostDTO postDTO = new PostDTO();
			postDTO.setCreatedAt(Instant.now());
			postDTO.setPostId("blh blah");
			Feed feed = new Feed("testUser", Instant.now());
			feed.setPosts(List.of(postDTO));

			repository.save(feed);
		};
	}

}
