package com.bmutlay.feed.repository;

import com.bmutlay.feed.model.Feed;
import com.bmutlay.feed.model.PostDTO;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FeedRepository extends CassandraRepository<Feed, String> {
}
