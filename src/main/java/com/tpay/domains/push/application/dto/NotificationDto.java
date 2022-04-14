package com.tpay.domains.push.application.dto;

import com.tpay.commons.push.PushCategoryType;
import com.tpay.commons.push.PushType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NotificationDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        String title;
        String body;
        PushType pushType;
        String pushTypeValue;
        String pushCategory;
        String link;

        public Request(PushCategoryType pushCategoryType, PushType pushType, String pushTypeValue) {
            this.title = pushCategoryType.getTitle();
            this.body = pushCategoryType.getBody();
            this.pushType = pushType;
            this.pushTypeValue = pushTypeValue;
            this.pushCategory = pushCategoryType.getPushCategory();
            this.link = pushCategoryType.getLink();
        }

        public Request(PushCategoryType pushCategoryType, PushType pushType, String pushTypeValue, String franchiseeStoreName) {
            this.title = franchiseeStoreName + pushCategoryType.getTitle();
            this.body = pushCategoryType.getBody();
            this.pushType = pushType;
            this.pushTypeValue = pushTypeValue;
            this.pushCategory = pushCategoryType.getPushCategory();
            this.link = pushCategoryType.getLink();
        }
    }

}