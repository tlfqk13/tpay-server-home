package com.tpay.domains.push.test.presentation;

import com.tpay.domains.push.test.application.TopicSubscribeService;
import com.tpay.domains.push.test.application.dto.TopicDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Service
public class TopicSubscribeController {

    private final TopicSubscribeService topicSubscribeService;

    @PostMapping("/fcm/topic")
    public void requestSubscribeTopic(@RequestBody TopicDto.Request request) {

        topicSubscribeService.subscribe(request);
    }

}
