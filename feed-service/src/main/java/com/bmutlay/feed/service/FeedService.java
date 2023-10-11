package com.bmutlay.feed.service;

import com.bmutlay.feed.model.Feed;
import com.bmutlay.feed.repository.FeedRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class FeedService {

    private final FeedRepository feedRepository;

    public FeedService(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    public Feed getFeedByUsername(String username) {
        Optional<Feed> optionalFeed = feedRepository.findById(username);
        if (!optionalFeed.isPresent()){
            throw new RuntimeException();
        }
        return optionalFeed.get();
    }

    public Feed createFeed(String username) {
        Feed feed = new Feed();
        //feed.setPosts(new ArrayList<>());
        feed.setUsername(username);
        return feedRepository.save(feed);
    }
}
