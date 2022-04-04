//package com.tpay.domains.push_batch.presentation;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.tpay.domains.push.application.PushSendService;
//import com.tpay.commons.push.detail.PushMessageRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.LocalDate;
//
//@RestController
//@RequiredArgsConstructor
//public class PushBatchTestApi {
//
//    private final ObjectMapper objectMapper;
//    private final PushSendService pushSendService;
//
//    @GetMapping("push/batch/api")
//    public ResponseEntity<String> testPushApi() {
//        String json = "{\n" +
//            "    \"userSelector\": \"FRANCHISEE\", \n" +
//            "    \"franchiseeIndex\": 92,\n" +
//            "    \"pushMessageIndex\": 12\n" +
//            "}";
//
//        try {
//            PushMessageRequest pushMessageRequest = objectMapper.readValue(json, PushMessageRequest.class);
//            pushSendService.sendMessageTo(pushMessageRequest);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return ResponseEntity.ok(LocalDate.now() + " 에 push 성공");
//    }
//}
