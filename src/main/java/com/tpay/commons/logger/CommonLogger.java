package com.tpay.commons.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * 공통 로거 클래스로 2022.06 부터 적용하여 이후 로직에 대해 로깅할 때 적용
 */
@Slf4j
@Component
public class CommonLogger {

    public void headline(Long externalRefundIndex,String headline) {
        log.trace("================INDEX : {}, {}===========================", externalRefundIndex, headline);
    }

    public void beforeHttpClient(Long externalRefundIndex) {
        log.trace("INDEX : {},HTTP 요청 전", externalRefundIndex);
    }

    public void afterHttpClient(Long externalRefundIndex) {
        log.trace("INDEX : {},HTTP 요청 후", externalRefundIndex);
    }

    public void point1(Long externalRefundIndex,String message) {
        log.trace("INDEX : {}, 지점1 : {}", externalRefundIndex, message);
    }

    public void point2(Long externalRefundIndex,String message) {
        log.trace("INDEX : {}, 지점2 : {}", externalRefundIndex, message);
    }

    public void error1(Long externalRefundIndex,String errorCode,String message) {
        log.error("INDEX : {}, 에러1 : "+"CODE["+errorCode+"] {}", externalRefundIndex, message);
    }


    public void tailLine(Long externalRefundIndex,String tailLine) {
        log.trace("================INDEX : {}, {}===========================", externalRefundIndex, tailLine);
    }

}
