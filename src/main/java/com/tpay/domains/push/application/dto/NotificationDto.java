package com.tpay.domains.push.application.dto;

import com.tpay.commons.push.PushCategoryType;
import com.tpay.commons.push.PushType;
import com.tpay.commons.util.converter.NumberFormatConverter;
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

        public Request(PushCategoryType pushCategoryType, PushType pushType, String pushTypeValue, Integer amount) {
            this.title = pushCategoryType.getTitle();
            this.body = pushCategoryType.getBody() + NumberFormatConverter.addCommaToNumber(amount.toString()) + "P";
            this.pushType = pushType;
            this.pushTypeValue = pushTypeValue;
            this.pushCategory = pushCategoryType.getPushCategory();
            this.link = pushCategoryType.getLink();
        }

        public Request setFrontTitle(String message) {
            this.title = message + title;
            return this;
        }

        public Request setFrontBody(String message){
            this.body = message + body;
            return this;
        }
    }

}