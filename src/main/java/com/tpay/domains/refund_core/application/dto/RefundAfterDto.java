package com.tpay.domains.refund_core.application.dto;

import com.tpay.domains.refund.domain.RefundAfterMethod;
import com.tpay.domains.van.domain.dto.VanRefundAfterBaseDto;
import com.tpay.domains.van.domain.dto.VanRefundAfterDto;
import com.tpay.domains.van.domain.dto.VanRefundFinalDto;
import lombok.Value;

public class RefundAfterDto {

    @Value
    public static class Request {
        RefundAfterBaseDto refundAfterInfo;
        RefundItemDto.Request refundItem;

        public Request(VanRefundAfterBaseDto vanRefundAfterBaseDto) {
            this.refundAfterInfo = RefundAfterBaseDto.builder()
                    .refundAfterMethod(RefundAfterMethod.valueOf(vanRefundAfterBaseDto.getRefundAfterMethod()))
                    .counterTypeCode("")
                    .cusCode(vanRefundAfterBaseDto.getCusCode())
                    .locaCode(vanRefundAfterBaseDto.getLocaCode())
                    .kioskCode(vanRefundAfterBaseDto.getKioskCode())
                    .kioskBsnmCode(vanRefundAfterBaseDto.getKioskBsnmCode())
                    .retry(false)
                    .refundFinishDate("")
                    .build();
            this.refundItem = null;
        }

        public Request(VanRefundAfterDto vanRefundAfterInfo, RefundItemDto.Request refundItem) {
            VanRefundAfterBaseDto vanBaseDto = vanRefundAfterInfo.getRefundAfterBaseInfo();
            this.refundAfterInfo = RefundAfterBaseDto.builder()
                    .refundAfterMethod(RefundAfterMethod.valueOf(vanBaseDto.getRefundAfterMethod()))
                    .counterTypeCode("")
                    .cusCode(vanBaseDto.getCusCode())
                    .locaCode(vanBaseDto.getLocaCode())
                    .kioskCode(vanBaseDto.getKioskCode())
                    .kioskBsnmCode(vanBaseDto.getKioskBsnmCode())
                    .retry(false)
                    .refundFinishDate("")
                    .build();
            this.refundItem = refundItem;
        }

        public Request(VanRefundFinalDto.Request vanRefundFinalDto, RefundItemDto.Request refundItem) {
            VanRefundAfterBaseDto vanBaseDto = vanRefundFinalDto.getBaseRefundAfterDto();
            this.refundAfterInfo = RefundAfterBaseDto.builder()
                    .refundAfterMethod(RefundAfterMethod.valueOf(vanBaseDto.getRefundAfterMethod()))
                    .counterTypeCode("")
                    .cusCode(vanBaseDto.getCusCode())
                    .locaCode(vanBaseDto.getLocaCode())
                    .kioskCode(vanBaseDto.getKioskCode())
                    .kioskBsnmCode(vanBaseDto.getKioskBsnmCode())
                    .retry(retryToBoolean(vanRefundFinalDto.getRetryYn()))
                    .refundFinishDate(vanRefundFinalDto.getRefundFinishDate())
                    .build();
            this.refundItem = refundItem;
        }

        private boolean retryToBoolean(String retryYn) {
            return retryYn.equals("Y");
        }
    }
}
