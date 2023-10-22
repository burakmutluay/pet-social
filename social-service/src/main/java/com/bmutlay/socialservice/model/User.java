package com.bmutlay.socialservice.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
@Data
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude="following")
public class User {
    @Id
    String username;
    @CreatedDate
    @Property("createdAt")
    Instant createdAt;
    @Relationship("FOLLOWS")
    Set<User> following;

    public Set<User> getFollowing() {
        if (following == null) {
            this.following = new HashSet<>();
        }
        return following;
    }
}
