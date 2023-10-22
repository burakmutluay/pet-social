package com.bmutlay.postservice.model;

import com.bmutlay.postservice.model.dto.PostDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserFollowEvent {

    public UserFollowEvent(String follower, String followed, Status status) {
        this.transactionId = UUID.randomUUID().toString();
        this.follower = follower;
        this.followed = followed;
        this.status = status;
        this.posts = new ArrayList<>();
    }

    private String transactionId;
    private String follower;
    private String followed;
    private Status status;
    private List<PostDTO> posts;

    public enum Status {
        FOLLOWED, UNFOLLOWED
    }
}
