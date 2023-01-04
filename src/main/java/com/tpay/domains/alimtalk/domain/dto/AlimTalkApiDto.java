package com.tpay.domains.alimtalk.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

public class AlimTalkApiDto {

    @Value
    @Builder
    @ToString
    public static class Request {
        String api_key; // 알림톡에서 발급된 api key
        String template_code; // 알림톡미 내 승인된 템플릿의 코드
        String variable; // 템플릿 내 대치될 변수, 여러개인 경우 | 로 묶어 전송
        String callback; // 문자 발신 번호
        String dstaddr; // 수신자 번호
        String next_type; // 알림톡 전송 실패시 문자 전송 - 1, 없음 - 0
        String send_reserve; // 0 - 즉시 발송, 1 - 예약 발송
        String send_reserve_date; // 예약 발송시 시간
    }

    @Getter
    @ToString
    public static class Response {
        String result;
    }
}
