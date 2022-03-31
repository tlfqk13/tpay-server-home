package com.tpay.domains.push.application;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.util.UserSelector;
import com.tpay.domains.employee.application.EmployeeFindService;
import com.tpay.domains.employee.domain.EmployeeEntity;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.push.application.dto.PushMessageBuilder;
import com.tpay.domains.push.application.dto.PushMessageRequest;
import com.tpay.domains.push.domain.PushMessageEntity;
import com.tpay.domains.push.domain.PushMessageRepository;
import com.tpay.domains.push.domain.PushTokenRepository;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.apache.http.HttpHeaders;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.tpay.commons.custom.CustomValue.PUSH_API_URI;
import static com.tpay.commons.custom.CustomValue.PUSH_CONFIG_PATH;

@Component
@RequiredArgsConstructor
public class PushSendService {
    private final ObjectMapper objectMapper;
    private final PushTokenRepository pushTokenRepository;
    private final FranchiseeFindService franchiseeFindService;
    private final EmployeeFindService employeeFindService;
    private final PushMessageRepository pushMessageRepository;

    public void sendMessageTo(PushMessageRequest pushMessageRequest) {

        String pushToken;
        String title;
        String body;

        // 프랜차이즈인지 직원인지에 따라 RequestDto로부터 인덱스 받아와서 토큰을 가져온다.
        if (pushMessageRequest.getUserSelector().equals(UserSelector.FRANCHISEE)) {
            FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(pushMessageRequest.getFranchiseeIndex());
            pushToken = pushTokenRepository.findByFranchiseeEntity(franchiseeEntity).orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid franchisee Index")).getPushToken();
        } else {
            EmployeeEntity employeeEntity = employeeFindService.findById(pushMessageRequest.getEmployeeIndex()).orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid Employee Index"));
            pushToken = pushTokenRepository.findByEmployeeEntity(employeeEntity).orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid Employee Index")).getPushToken();
        }

        //15일 경우 공지사항이므로 RequestDto안에 타이틀과 바디를 받아온다.
        if (pushMessageRequest.getPushMessageIndex().equals(15L)) {
            title = pushMessageRequest.getTitle();
            body = pushMessageRequest.getBody();
        }
        //나머지의 경우 push_message 테이블 안에서 title과 body를 가져온다.
        else {
            PushMessageEntity pushMessageEntity = pushMessageRepository.findById(pushMessageRequest.getPushMessageIndex()).orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid Push Message Index"));
            title = pushMessageEntity.getTitle();
            body = pushMessageEntity.getBody();
        }

        // 클래스 내의 makeMessage 메서드로 FCM에 보낼 메시지를 만든다. (규격은 FCM 문서 참고)
        String message = makeMessage(pushToken, title, body, pushMessageRequest.getPushMessageIndex());
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));

        try {
            System.out.println(PUSH_API_URI);
            Request request = new Request.Builder()
                .url(PUSH_API_URI)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

            Response response = okHttpClient.newCall(request)
                .execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO Exception !!!");
        }
    }

    private String makeMessage(String pushToken, String title, String body, Long pushIndex) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("pushIndex", pushIndex.toString());
        PushMessageBuilder pushMessageBuilder = PushMessageBuilder.builder()
            .message(
                PushMessageBuilder.Message.builder()
                    .data(data)
                    .token(pushToken)
                    .notification(
                        PushMessageBuilder.Notification.builder()
                            .title(title)
                            .body(body)
                            .image(null)
                            .build())
                    .apns(PushMessageBuilder.Apns.builder()
                        .payload(
                            PushMessageBuilder.Payload.builder()
                                .aps(PushMessageBuilder.Aps.builder().sound("default").build())
                                .build())
                        .build())
                    .build())
            .validate_only(false)
            .build();
        String fcmMessageString = "";

        try {
            fcmMessageString = objectMapper.writeValueAsString(pushMessageBuilder);
            System.out.println(fcmMessageString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return fcmMessageString;
    }


    //FCM에 AccessToken 요청하는 로직
    private String getAccessToken() throws IOException {
        String firebaseConfigPath = PUSH_CONFIG_PATH;
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream()).createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
