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

        public Request(PushCategoryType pushCategoryType, PushType pushType, String pushTypeValue, String title, String body) {
            this.title = title;
            this.body = body;
            this.pushType = pushType;
            this.pushTypeValue = pushTypeValue;
            this.pushCategory = pushCategoryType.getPushCategory();
            this.link = pushCategoryType.getLink();
        }

        public Request setFrontTitle(String message) {
            this.title = message + title;
            return this;
        }

        public Request setFrontBody(String message) {
            this.body = message + body;
            return this;
        }

        public Request setBehindBodyPoint(String amount) {
            this.body = body + NumberFormatConverter.addCommaToNumber(amount) + "P";
            return this;
        }

        public Request setBehindLink(String addLink) {
            this.link = link + "/" + addLink;
            return this;
        }
    }

}