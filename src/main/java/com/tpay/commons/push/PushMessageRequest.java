package com.tpay.commons.push;


import com.tpay.commons.util.UserSelector;
import lombok.Getter;

import java.util.List;

@Getter
public class PushMessageRequest {

    // 보낼 대상 셀렉터
    private PushTargetSelector pushTargetSelector;

    // 대상이 1개 device 경우 아래 필드가 채워짐
    private UserSelector userSelector;
    private Long franchiseeIndex;
    private Long employeeIndex;

    // 대상이 SEVERAL 인 경우 아래 필드가 채워짐
    private List<Long> franchiseeIndexList;
    private List<Long> employeeIndexList;

    // 필수로 채워짐
    private Long pushMessageIndex;

    // pushMessageIndex 15(수동 메시지)의 경우 채워짐
    private String title;
    private String body;
}
