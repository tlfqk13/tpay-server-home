package com.tpay.domains.refund_core.application.dto;

import com.tpay.domains.refund.domain.RefundAfterMethod;
import com.tpay.domains.van.domain.dto.VanRefundAfterBaseDto;
import com.tpay.domains.van.domain.dto.VanRefundFinalDto;
import lombok.Value;

public class RefundAfterDto {

    @Value
    public static class Request {
        RefundAfterBaseDto refundAfterInfo;
        RefundItemDto.Request refundItem;

        public Request(RefundAfterBaseDto refundAfterInfo, RefundItemDto.Request refundItem) {
            this.refundAfterInfo = refundAfterInfo;
            this.refundItem = refundItem;
        }

        public static RefundAfterDto.Request of(VanRefundAfterBaseDto vanRefundAfterBaseDto, RefundItemDto.Request refundItem) {
            RefundAfterBaseDto baseDto = RefundAfterBaseDto.builder()
                    .refundAfterMethod(RefundAfterMethod.valueOf(vanRefundAfterBaseDto.getRefundAfterMethod()))
                    .counterTypeCode("")
                    .cusCode(vanRefundAfterBaseDto.getCusCode())
                    .locaCode(vanRefundAfterBaseDto.getLocaCode())
                    .kioskCode(vanRefundAfterBaseDto.getKioskCode())
                    .kioskBsnmCode(vanRefundAfterBaseDto.getKioskBsnmCode())
                    .retry(false)
                    .refundFinishDate("")
                    .build();

            return new RefundAfterDto.Request(baseDto, refundItem);
        }

        public static RefundAfterDto.Request of(VanRefundFinalDto.Request vanRefundFinalDto, RefundItemDto.Request refundItem) {
            VanRefundAfterBaseDto vanBaseDto = vanRefundFinalDto.getBaseRefundAfterDto();
            RefundAfterBaseDto baseDto = RefundAfterBaseDto.builder()
                    .refundAfterMethod(RefundAfterMethod.valueOf(vanBaseDto.getRefundAfterMethod()))
                    .counterTypeCode("")
                    .cusCode(vanBaseDto.getCusCode())
                    .locaCode(vanBaseDto.getLocaCode())
                    .kioskCode(vanBaseDto.getKioskCode())
                    .kioskBsnmCode(vanBaseDto.getKioskBsnmCode())
                    .retry(retryToBoolean(vanRefundFinalDto.getRetryYn()))
                    .refundFinishDate(vanRefundFinalDto.getRefundFinishDate())
                    .build();

            return new RefundAfterDto.Request(baseDto, refundItem);
        }

        private static boolean retryToBoolean(String retryYn) {
            return retryYn.equals("Y");
        }
    }
}
