package com.bmutlay.feed.model;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.*;

import java.time.Instant;

@Data
@RequiredArgsConstructor
@Getter
@Setter
@Tuple
public class PostDTO {
    @Element(0)
    private String postId;

    @CreatedDate
    @Element(1)
    private Instant createdAt;

}
