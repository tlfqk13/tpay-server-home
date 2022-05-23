package com.tpay.domains.notice.application.dto;

import com.tpay.domains.notice.domain.NoticeEntity;
import lombok.Getter;

import java.util.List;

public class AppNoticeFindDto {

    @Getter
    public static class FindAllResponse {

        List<NoticeEntity> fixedList;
        List<NoticeEntity> nonFixedList;

        public FindAllResponse(List<NoticeEntity> fixedList, List<NoticeEntity> nonFixedList) {
            this.fixedList = fixedList;
            this.nonFixedList = nonFixedList;
        }
    }

    @Getter
    public static class FindOneResponse {
        NoticeEntity noticeEntity;

        public FindOneResponse(NoticeEntity noticeEntity) {
            this.noticeEntity = noticeEntity;
        }
    }
}
