package com.bmutlay.socialservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
public class PostCreatedEvent {

    public PostCreatedEvent(String postId, String createdBy) {
        this.postId = postId;
        this.createdBy = createdBy;
        this.transactionId = UUID.randomUUID().toString();
        this.followers = new HashSet<>();
    }

    private String transactionId;
    private String postId;
    private String createdBy;
    private Set<String> followers;

}
