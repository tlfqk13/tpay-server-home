package com.tpay.domains.refund.application.dto;

import com.tpay.domains.refund.domain.RefundStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.MODULE)
@Getter
public class RefundByCustomerDateResponse {
    private String date;
    private List<Data> dataList;

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class Data {
        private Long refundIndex;
        private String orderNumber;
        private LocalDateTime createdDate;
        private LocalDate formatDate;
        private String totalAmount;
        private String totalRefund;
        private RefundStatus refundStatus;

        public Data(RefundByCustomerResponse response) {
            this.refundIndex = response.getRefundIndex();
            this.orderNumber = response.getOrderNumber();
            this.createdDate = response.getCreatedDate();
            this.formatDate = response.getFormatDate();
            this.totalAmount = response.getTotalAmount();
            this.totalRefund = response.getTotalRefund();
            this.refundStatus = response.getRefundStatus();
        }
    }
}
