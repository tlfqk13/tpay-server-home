package com.tpay.domains.alimtalk.presentation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpay.commons.custom.CustomValue;
import com.tpay.domains.alimtalk.domain.AlimTalkTemplate;
import com.tpay.domains.alimtalk.domain.dto.AlimTalkApiDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlimTalkService {

    private final WebClient.Builder builder;

    public void sendAlimTalkApiMessage(AlimTalkApiDto.Request dto) {
        ObjectMapper mapper = new ObjectMapper();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        Map<String, String> map = mapper.convertValue(dto, new TypeReference<Map<String, String>>() {});
        params.setAll(map);
        log.debug("params = {}", params);

        String response = builder
                .build()
                .post()
                .uri(CustomValue.ALIMTALK_SEVER + "/alimtalk_api")
                .bodyValue(params)
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.debug("Alimtalk response = {}", response);
    }

    public AlimTalkApiDto.Request createAlimtalkMessage(String variable, String storePhoneNumber) {
        AlimTalkTemplate alimtalkTemplate = getAlimtalkTemplate();
        return AlimTalkApiDto.Request.builder()
                .api_key(CustomValue.ALIMTALK_KEY)
                .template_code(alimtalkTemplate.name())
                .variable(variable)
                .callback("01082692671")
                .dstaddr(storePhoneNumber)
                .next_type("1")
                .send_reserve("0")
                .build();
    }

    public AlimTalkApiDto.Request createAlimtalkMessageForTest(String variable, AlimTalkTemplate alimTalkTemplate, String storePhoneNumber) {
        return AlimTalkApiDto.Request.builder()
                .api_key(CustomValue.ALIMTALK_KEY)
                .template_code(alimTalkTemplate.name())
                .variable(variable)
                .callback("01082692671")
                .dstaddr(storePhoneNumber)
                .next_type("1")
                .send_reserve("0")
                .build();
    }

    private AlimTalkTemplate getAlimtalkTemplate() {
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        int hour = now.getHour();
        if (dayOfWeek.getValue() < 6) {
            // 평일
            if (hour < 17) {
                // 17시 이전
                return AlimTalkTemplate.SJT_085720;
            } else {
                // 17시 이후
                if (DayOfWeek.FRIDAY == dayOfWeek) {
                    return AlimTalkTemplate.SJT_085721;
                }
                return AlimTalkTemplate.SJT_085686;
            }
        } else {
            // 주말
            return AlimTalkTemplate.SJT_085721;
        }
    }
}
