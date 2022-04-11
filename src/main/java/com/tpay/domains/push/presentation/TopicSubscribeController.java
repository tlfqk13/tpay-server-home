package com.tpay.domains.push.presentation;

import com.tpay.domains.push.application.dto.TopicDto;
import com.tpay.domains.push.application.TopicSubscribeService;
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
