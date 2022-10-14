package com.tpay.commons.push;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PushCategoryType {
    CASE_ONE("1", "ktp://notification", "점주님, 아직 가맹점을 신청하지 않으셨어요\uD83E\uDD72", "가맹점을 신청하고 KTP 혜택을 누려보세요!"),
    CASE_TWO("2", "ktp://notification", "\uD83D\uDC4FKTP 가맹점주님이 되신 것을 축하드립니다!\uD83D\uDC4F", "가맹점으로 승인되었습니다. 이제 KTP와 함께 사후면세점을 운영해보세요."),
    CASE_THREE("3", "ktp://notification", " 점주님, 가맹점 신청이 거절되었어요\uD83E\uDD72", "가맹점을 다시 신청하실 수 있도록 도와드릴게요!"),
    CASE_FOUR("4", "ktp://notification", "점주님, 가맹점을 다시 신청해주세요\uD83E\uDD72", "혹시 가맹점을 다시 신청하는데 어려움이 있으신가요? 고객센터로 연락주시면 빠르게 도와드릴게요!"),
    CASE_FIVE("5", "ktp://notification", "\uD83D\uDC4F처음 환급이 완료되었습니다! T.POINT가 2주 뒤에 적립될 예정이에요\uD83D\uDC4F", "T.POINT가 무엇인지와 유효기간에 대해 알려드릴게요."),
    CASE_SIX("6", "ktp://notification", "\uD83D\uDC49T.POINT를 어떻게 사용할 수 있는지 궁금하신가요?", "적립된 T.POINT의 사용방법과 정책에 대해 알려드릴게요!"),
    CASE_SEVEN("7", "ktp://notification", "T.POINT가 정상적으로 출금되었어요\uD83D\uDE00", "출금된 T.POINT : "),
    CASE_EIGHT("8", "ktp://notification", " 점주님, 소멸 예정인 T.POINT가 있어요‼️", "한달 이내로 소멸예정인 T.POINT를 출금해주세요."),
    CASE_NINE("9", "ktp://notification", "과연 이번달에는 지난달보다 매출이 높아졌을까요?\uD83E\uDD14", "이번달의 매출과 환급건수를 지난달과 비교해보세요!"),
    CASE_TEN("10", "ktp://notification", "과연 올해에는 작년보다 매출이 높아졌을까요?\uD83E\uDD14", "올해의 매출과 환급건수를 작년과 비교해보세요!"),
    CASE_ELEVEN("11", "ktp://notification", " 점주님, 부가세 신고기간이에요!\uD83D\uDE00", "지금 부가세 신고자료를 다운로드 받아보세요."),
    CASE_TWELVE("12", "ktp://notification", "\uD83D\uDC49내일은 환급수수료가 이체되는 날이에요", "마이페이지에서 ‘CMS 청구내역’을 확인해보세요!"),
    CASE_THIRTEEN("13", "ktp://notification", "환급수수료가 정상적으로 이체되었어요\uD83D\uDE00", "출금된 금액 : "),
    CASE_FOURTEEN("14", "ktp://notification", "직원이 로그인했어요\uD83D\uDC4B", "님이 직원 계정으로 로그인을 했어요."),
    CASE_FIFTEEN("15", "ktp://notification", "", ""),


    /**
     * 개발용, 사용자 노출이 아닌 back 에서 front 로 알림이 필요한 경우
     * pushCategory 9000번대 부터 사용
     */
    REFUND_CANCEL_SCREEN_REFRESH_WITH_EXTERNAL("9000", "", "", ""),

    FORCE_UPDATE_FOR_KILL_APP("9001","","","")
    ;

    private final String pushCategory;
    private final String link;

    private final String title;
    private final String body;


}
