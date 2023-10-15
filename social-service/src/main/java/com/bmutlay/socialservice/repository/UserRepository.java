package com.bmutlay.socialservice.repository;

import com.bmutlay.socialservice.model.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends Neo4jRepository<User, String> {

    Optional<User> findByUsername(String username);

    @Query("MATCH (follower:User {username: $follower})-[r:FOLLOWS]->(:User {username:$unfollowed}) DELETE r")
    void unfollow(String follower, String unfollowed);

    @Query("MATCH (u:User {username:$username})<-[r:FOLLOWS]-(f:User) RETURN f")
    List<User> findFollowers(String username);

}
