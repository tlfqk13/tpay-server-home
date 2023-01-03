package com.tpay.domains.vat.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.tpay.domains.vat.constant.HomeTaxConstant.VatConstType.CHAR;
import static com.tpay.domains.vat.constant.HomeTaxConstant.VatConstType.NUMERIC;

@Getter
@RequiredArgsConstructor
public enum HomeTaxConstant {
    RECORD_TOTAL_LENGTH(NUMERIC, 0, 200),

    // 공통 사용 항목
    RECORD_SECTION(CHAR, 0, 2), // 레코드 구분
    BELONGING_YEAR(CHAR, 2, 4), // 귀속년도
    HALF_YEAR_SECTION(CHAR, 6, 1), // 반기구분
    MONTH_IN_HALF_YEAR(CHAR, 7, 1), // 반기 내 월 순번
    BUSINESS_REG_NUMBER(CHAR, 8, 10), // 사업자 등록번호

    // HEAD
    STORE_NAME(CHAR, 18, 60), // 법인명(상호)
    OWNER_NAME(CHAR, 78, 30), // 대표자 성명
    OWNER_RESIDENT_OR_CORPORATION_NUMBER(CHAR, 108, 13, false),  // 주민 또는 법인 등록번호
    WRITE_DATE(CHAR, 121, 8), // 작성 일자
    STORE_TEL(CHAR, 129, 12), // 법인 전화번호
    HEAD_RECORD_SPACE(CHAR, 141, 59), // HEAD RECORD 공란

    // DATA - 공통
    SUB_COMPANY_NUMBER(CHAR, 18, 4), // 종사업자 일련번호
    SERIAL_NUMBER(CHAR, 22, 4), // 일련번호
    PURCHASE_SERIAL_NUMBER(CHAR, 26, 20), //구매 일련번호
    SALE_DATE(CHAR, 46, 8), // 판매일자

    // DATA - 즉시(I)
    CARRY_OUT_PERMISSION_NUMBER(CHAR, 54, 20), // 반출승인번호
    PRICE_WITH_TAX(NUMERIC, 74, 15), // 세금 포함 판매가격
    VAT(NUMERIC, 89, 15), // 부가가치세
    TAX_REFUND_AMOUNT(NUMERIC, 104, 15), // 환급액
    BUYER_NAME(CHAR, 119, 30, false), // 구매자 성명
    BUYER_COUNTRY(CHAR, 149, 30, false), // 구매자 국적
    DATA_RECORD_SPACE(CHAR, 179, 21), // DATA RECORD 공란

    // DATA - 일반(F), 즉시와 중복되는 부분은 F를 붙임
    TAKEOUT_DATE(CHAR, 54, 8), // 반출 일자
    F_CARRY_OUT_PERMISSION_NUMBER(CHAR, 62, 20), // 반출승인번호(일반)
    REFUND_DATE(CHAR, 82, 8), // 환급(송금) 일자
    F_TAX_REFUND_AMOUNT(NUMERIC, 90, 15), // 환급액
    F_PRICE_WITH_TAX(NUMERIC, 105, 15),
    F_VAT(NUMERIC, 120, 15),
    IND(NUMERIC, 135, 15),
    EDU(NUMERIC, 150, 15),
    RURAL(NUMERIC, 165, 15),
    F_DATA_RECORD_SPACE(CHAR, 179, 21), // DATA RECORD 공란


    // TAIL
    TAX_FREE_STORE_NUMBER(CHAR, 22, 8), // 면세 판매장 지정번호
    TOTAL_COUNT(NUMERIC, 30, 11), // 합계 건수
    TOTAL_PRICE_WITH_TAX(NUMERIC, 41, 15), // 세금 포함 판매가격 합계
    TOTAL_VAT(NUMERIC, 56, 15), // 부가가치세 합계
    TOTAL_TAX_REFUND_AMOUNT(NUMERIC, 71, 15), // 즉시환급액 합계
    REFUND_BUSINESS_NUMBER(NUMERIC, 86, 10), // 환급 창구 운영 사업자등록번호
    TAIL_RECORD_SPACE(CHAR, 96, 104)
    ;

    public enum VatConstType {

        CHAR,
        NUMERIC
    }

    /**
     *
     * @param constType - 문서에서 지정한 데이터의 형태
     * @param start - 데이터 입력의 시작 지점
     * @param len - 데이터의 총 길이(실제 길이와는 다를 수도 있음)
     * isNeed - 필수 데이터인가 아닌가, 아닌 경우 데이터가 없어도 padding 으로 채우고 넘어감
     */
    HomeTaxConstant(VatConstType constType, int start, int len) {
        this.constType = constType;
        this.start = start;
        this.len = len;
        this.isNeed = true;
    }

    private final VatConstType constType;
    private final int start;
    private final int len;
    private final boolean isNeed;

}
