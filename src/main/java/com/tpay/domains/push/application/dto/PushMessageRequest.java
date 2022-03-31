package com.tpay.domains.push.application.dto;


import com.tpay.commons.util.UserSelector;
import lombok.Getter;

@Getter
public class PushMessageRequest {
    private UserSelector userSelector;
    private Long franchiseeIndex;
    private Long employeeIndex;
    private Long pushMessageIndex;
    private String title;
    private String body;
}
