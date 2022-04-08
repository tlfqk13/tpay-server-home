package com.tpay.commons.push;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PushCategoryType {
    CASE_ONE("1", "ktp://franchisee/application", " 점주님, 아직 가맹점을 신청하지 않으셨어요", "가맹점을 신청하고 KTP 혜택을 누려보세요!"),
    CASE_TWO("2", "ktp://mypage", "KTP 가맹점주님이 되신 것을 축하드립니다!", "가맹점으로 승인되었습니다. 이제 KTP와 함께 사후면세점을 운영해보세요."),
    CASE_THREE("3", "ktp://notification/detail/", " 점주님, 가맹점 신청이 거절되었어요", "가맹점을 다시 신청하실 수 있도록 도와드릴게요!");


    private final String pushCategory;
    private final String link;

    private final String title;
    private final String body;



}
