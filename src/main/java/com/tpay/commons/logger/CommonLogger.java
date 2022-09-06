package com.tpay.commons.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * 공통 로거 클래스로 2022.06 부터 적용하여 이후 로직에 대해 로깅할 때 적용
 */
@Slf4j
@Component
public class CommonLogger {

    public void headline(Long index,String headline) {
        log.trace("================INDEX : {}, {}===========================", index, headline);
    }

    public void beforeHttpClient(Long index) {
        log.trace("INDEX : {},HTTP 요청 전", index);
    }

    public void afterHttpClient(Long index) {
        log.trace("INDEX : {},HTTP 요청 후", index);
    }

    public void point1(Long index,String message) {
        log.trace("INDEX : {}, 지점1 : {}", index, message);
    }

    public void point2(Long index,String message) {
        log.trace("INDEX : {}, 지점2 : {}", index, message);
    }

    public void error1(Long index,String errorCode,String message) {
        log.error("INDEX : {}, 에러1 : "+"CODE["+errorCode+"] {}", index, message);
    }


    public void tailLine(Long index,String tailLine) {
        log.trace("================INDEX : {}, {}===========================", index, tailLine);
    }

}
