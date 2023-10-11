package com.bmutlay.feed.model;

import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Table
@Getter
@Setter
@Data
public class Feed {
    @PrimaryKey
    private String username;

    public Feed() {
    }

    public Feed(String username, Instant latestUpdateDate) {
        this.username = username;
        this.latestUpdateDate = latestUpdateDate;
    }
    @LastModifiedDate
    private Instant latestUpdateDate;
    @Transient
    private List<PostDTO> posts;

}
