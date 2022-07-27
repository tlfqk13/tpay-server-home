package com.tpay.domains.notice.application.dto;

import com.tpay.domains.notice.domain.NoticeEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class CommonNoticeFindDto {

    @Getter
    public static class FindAllResponse {
        private final Long noticeIndex;
        private final String title;
        private final LocalDateTime createdDate;
        private final LocalDateTime scheduledDate;
        private final Boolean isFixed;
        private final Boolean isInvisible;

        public FindAllResponse(NoticeEntity noticeEntity) {
            this.noticeIndex = noticeEntity.getId();
            this.title = noticeEntity.getTitle();
            this.createdDate = noticeEntity.getCreatedDate();
            this.scheduledDate = noticeEntity.getScheduledDate();
            this.isFixed = noticeEntity.getIsFixed();
            this.isInvisible = noticeEntity.getIsInvisible();
        }
    }

    @Getter
    public static class FindOneResponse {
        private final Boolean isFixed;
        private final Boolean isImmediate;
        private final LocalDateTime createdDate;
        private final LocalDateTime scheduledDate;
        private final String title;
        private final String subTitle1;
        private final String subTitle2;
        private final String subTitle3;
        private final String content1;
        private final String content2;
        private final String content3;
        private final String link;
        private final Boolean isInvisible;
        private final String mainImg;
        private final String subImg1;
        private final String subImg2;
        private final String subImg3;

        public FindOneResponse(NoticeEntity noticeEntity) {
            this.isFixed = noticeEntity.getIsFixed();
            this.isImmediate = noticeEntity.getIsImmediate();
            this.createdDate = noticeEntity.getCreatedDate();
            this.scheduledDate = noticeEntity.getScheduledDate();
            this.title = noticeEntity.getTitle();
            this.subTitle1 = noticeEntity.getSubTitle1();
            this.subTitle2 = noticeEntity.getSubTitle2();
            this.subTitle3 = noticeEntity.getSubTitle3();
            this.content1 = noticeEntity.getContent1();
            this.content2 = noticeEntity.getContent2();
            this.content3 = noticeEntity.getContent3();
            this.link = noticeEntity.getLink();
            this.isInvisible = noticeEntity.getIsInvisible();
            this.mainImg = noticeEntity.getMainImage().equals("") ? "" : noticeEntity.getBaseS3Uri() + noticeEntity.getMainImage();
            this.subImg1 = noticeEntity.getSubImg1().equals("") ? "" : noticeEntity.getBaseS3Uri() + noticeEntity.getSubImg1();
            this.subImg2 = noticeEntity.getSubImg2().equals("") ? "" : noticeEntity.getBaseS3Uri() + noticeEntity.getSubImg2();
            this.subImg3 = noticeEntity.getSubImg3().equals("") ? "" : noticeEntity.getBaseS3Uri() + noticeEntity.getSubImg3();
        }
    }
}
