package com.tpay.domains.push.test.presentation;

import com.tpay.domains.push.test.application.TopicUnsubscribeService;
import com.tpay.domains.push.test.application.dto.TopicDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TopicUnsubscribeController {

    private final TopicUnsubscribeService topicUnsubscribeService;

    @PatchMapping("/fcm/topic")
    public void requestUnsubscribeTopic(@RequestBody TopicDto.Request request) {
        topicUnsubscribeService.unsubscribe(request);
    }
}
