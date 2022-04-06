package com.tpay.domains.push.test.presentation;

import com.tpay.domains.push.test.application.PushTopicService;
import com.tpay.domains.push.test.application.dto.TopicDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FcmTopicApiController {

    private final PushTopicService pushTopicService;

    @PostMapping("/fcm/topic")
    public void requestSubscribeTopic(@RequestBody TopicDto.Request request) {

        pushTopicService.subscribeToTopic(request);
    }

    @DeleteMapping("/fcm/topic")
    public void requestUnsubscribeTopic(@RequestBody TopicDto.Request request) {
        pushTopicService.unsubscribeFromTopic(request);
    }
}
