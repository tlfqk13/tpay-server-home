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

    private Long pushMessageIndex;
    private String title;
    private String body;
}
