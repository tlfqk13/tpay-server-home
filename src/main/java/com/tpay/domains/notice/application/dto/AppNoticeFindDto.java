package com.tpay.domains.notice.application.dto;

import lombok.Getter;

import java.util.List;

public class AppNoticeFindDto {

    @Getter
    public static class FindAllResponse {

        List<CommonNoticeFindDto.FindAllResponse> fixedList;
        List<CommonNoticeFindDto.FindAllResponse> nonFixedList;

        public FindAllResponse(List<CommonNoticeFindDto.FindAllResponse> fixedList, List<CommonNoticeFindDto.FindAllResponse> nonFixedList) {
            this.fixedList = fixedList;
            this.nonFixedList = nonFixedList;
        }
    }

}
