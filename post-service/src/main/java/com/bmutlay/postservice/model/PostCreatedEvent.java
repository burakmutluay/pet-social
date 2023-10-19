package com.bmutlay.postservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class PostCreatedEvent {

    public PostCreatedEvent(String postId, String createdBy) {
        this.postId = postId;
        this.createdBy = createdBy;
        this.transactionId = UUID.randomUUID().toString();
        this.followers = new ArrayList<>();
    }

    private String transactionId;
    private String postId;
    private String createdBy;
    private List<String> followers;

}
