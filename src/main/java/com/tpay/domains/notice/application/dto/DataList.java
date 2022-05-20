package com.tpay.domains.notice.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DataList {
    private Boolean isFixed;
    private Boolean isImmediate;
    private Boolean isInvisible;
    private String scheduledDate;
    private String title;
    private String subTitle1;
    private String subTitle2;
    private String subTitle3;
    private String content1;
    private String content2;
    private String content3;
    private String link;
}
