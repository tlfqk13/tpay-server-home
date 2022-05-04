package com.tpay.domains.push.application.dto;


import java.time.LocalDateTime;

public interface AdminPushDto {
    Long getId();

    LocalDateTime getCreatedDate();

    String getTitle();
}
