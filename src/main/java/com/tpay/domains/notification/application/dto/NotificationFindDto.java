package com.tpay.domains.notification.application.dto;

import com.tpay.domains.notification.domain.NotificationEntity;
import lombok.Getter;

import java.time.LocalDateTime;

public class NotificationFindDto {

    @Getter
    public static class FindAllResponse {
        private final Long notificationIndex;
        private final String title;
        private final LocalDateTime createdDate;
        private final LocalDateTime scheduledDate;
        private final Boolean isFixed;
        private final Boolean isInvisible;

        public FindAllResponse(NotificationEntity notificationEntity) {
            this.notificationIndex = notificationEntity.getId();
            this.title = notificationEntity.getTitle();
            this.createdDate = notificationEntity.getCreatedDate();
            this.scheduledDate = notificationEntity.getScheduledDate();
            this.isFixed = notificationEntity.getIsFixed();
            this.isInvisible = notificationEntity.getIsInvisible();
        }
    }

    @Getter
    public static class FindOneResponse {
        private final Boolean isFixed;
        private final Boolean isImmediate;
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

        public FindOneResponse(NotificationEntity notificationEntity) {
            this.isFixed = notificationEntity.getIsFixed();
            this.isImmediate = notificationEntity.getIsImmediate();
            this.scheduledDate = notificationEntity.getScheduledDate();
            this.title = notificationEntity.getTitle();
            this.subTitle1 = notificationEntity.getSubTitle1();
            this.subTitle2 = notificationEntity.getSubTitle2();
            this.subTitle3 = notificationEntity.getSubTitle3();
            this.content1 = notificationEntity.getContent1();
            this.content2 = notificationEntity.getContent2();
            this.content3 = notificationEntity.getContent3();
            this.link = notificationEntity.getLink();
            this.isInvisible = notificationEntity.getIsInvisible();
            this.mainImg = notificationEntity.getMainImage().equals("") ? "" : notificationEntity.getBaseS3Uri() + notificationEntity.getMainImage();
            this.subImg1 = notificationEntity.getSubImg1().equals("") ? "" : notificationEntity.getBaseS3Uri() + notificationEntity.getSubImg1();
            this.subImg2 = notificationEntity.getSubImg2().equals("") ? "" : notificationEntity.getBaseS3Uri() + notificationEntity.getSubImg2();
            this.subImg3 = notificationEntity.getSubImg3().equals("") ? "" : notificationEntity.getBaseS3Uri() + notificationEntity.getSubImg3();
        }
    }
}
