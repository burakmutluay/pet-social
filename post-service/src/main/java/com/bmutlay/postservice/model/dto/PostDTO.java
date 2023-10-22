package com.bmutlay.postservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class PostDTO {
    String postId;
    Instant createdAt;
}
