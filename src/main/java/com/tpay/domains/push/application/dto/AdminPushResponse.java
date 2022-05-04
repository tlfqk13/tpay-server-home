package com.tpay.domains.push.application.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminPushResponse {
    private Long pushIndex;
    private LocalDateTime createdDate;
    private String title;


    public AdminPushResponse(AdminPushDto adminPushDto) {
        this.pushIndex = adminPushDto.getId();
        this.createdDate = adminPushDto.getCreatedDate();
        this.title = adminPushDto.getTitle();
    }
}
