package com.bmutlay.socialservice.model;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.Instant;
import java.util.Set;
@Data
@RequiredArgsConstructor
@Getter
@Setter
public class User {
    @Id
    String username;
    @CreatedDate
    @Property("createdAt")
    Instant createdAt;
    @Relationship("FOLLOWS")
    Set<User> following;
}
