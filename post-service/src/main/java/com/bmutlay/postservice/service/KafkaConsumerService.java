package com.bmutlay.postservice.service;

import com.bmutlay.postservice.model.UserFollowEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final PostService postService;

    @KafkaListener(topics = {"follow-event-topic"}, groupId = "group-id-1")
    public void consumer(UserFollowEvent userFollowEvent) {
        postService.populateAndPublishUserFollowEvent(userFollowEvent);
    }
}
