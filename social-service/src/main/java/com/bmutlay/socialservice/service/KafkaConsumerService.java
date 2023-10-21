package com.bmutlay.socialservice.service;

import com.bmutlay.socialservice.model.PostCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final UserService userService;

    @KafkaListener(topics = {"post-topic"}, groupId = "group-id")
    public void consumer(PostCreatedEvent postCreatedEvent) {
        userService.populatePostCreatedEventAndSend(postCreatedEvent);
    }

}
