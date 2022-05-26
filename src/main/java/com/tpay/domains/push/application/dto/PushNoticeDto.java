package com.tpay.domains.push.application.dto;

import com.tpay.domains.notice.domain.NoticeEntity;
import lombok.Getter;

import java.util.List;

@Getter
public class PushNoticeDto {

    List<PushNoticeResponse> pushNoticeResponseList;

    public PushNoticeDto(List<PushNoticeResponse> pushNoticeResponseList) {
        this.pushNoticeResponseList = pushNoticeResponseList;
    }

    @Getter
    public static class PushNoticeResponse {
        private final Long noticeIndex;
        private final String title;

        public PushNoticeResponse(NoticeEntity noticeEntity) {
            this.noticeIndex = noticeEntity.getId();
            this.title = noticeEntity.getTitle();
        }
    }
}
