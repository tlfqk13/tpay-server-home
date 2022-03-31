package com.tpay.domains.push.presentation;


import com.tpay.domains.push.application.PushSendService;
import com.tpay.domains.push.application.dto.PushMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ManualPushController {

    private final PushSendService pushSendService;

    //어드민에서 수동으로 푸시메시지 작성시 사용됨
    @PostMapping("/admin/push")
    public void testSend(@RequestBody PushMessageRequest pushMessageRequest){
            pushSendService.sendMessageTo(pushMessageRequest);
    }
}
