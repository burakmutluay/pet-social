package com.bmutlay.socialservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserFollowEvent {

    public UserFollowEvent(String follower, String followed, Status status) {
        this.transactionId = UUID.randomUUID().toString();
        this.follower = follower;
        this.followed = followed;
        this.status = status;
    }

    private String transactionId;
    private String follower;
    private String followed;
    private Status status;
    public enum Status {
        FOLLOWED, UNFOLLOWED
    }
}
