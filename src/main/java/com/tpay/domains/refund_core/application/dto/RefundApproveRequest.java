package com.tpay.domains.refund_core.application.dto;

import com.tpay.commons.custom.CustomValue;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.order.domain.OrderEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.tpay.domains.refund.domain.RefundAfterMethod.*;

@Getter
@Builder
@Slf4j
public class RefundApproveRequest {
    private String serviceName;
    private String businessNumber;
    private String franchiseeName;
    private String franchiseeNumber;
    private String sellerName;
    private String storeName;
    private String storeAddress;
    private String storeTel;

    private String name;
    private String nationality;
    private String totalAmount;
    private String passportNumber;

    private String totalRefund;
    private String totalVat;
    private String totalIct;
    private String totalStr;
    private String totalEdut;
    private String totalQuantity;
    private String saleDate;

    private String indList;
    private String indListNow;
    List<RefundProductInfo> productInfoList;

    //After Refund Info

    private String cusCode;
    private String purchaseSequenceNumber;
    private String locaCode;
    private String kioskBsnmCode;
    private String kioskCode;
    private String cityRefundCenterCode;
    private String manualYn;
    private String cityYn;
    private String kioskYn;
    private String retryYn;
    private String afilYn; // 가맹점 실시간 접수 현황

    public static RefundApproveRequest of(OrderEntity orderEntity, RefundAfterDto.Request refundAfterDto, boolean isVan) {
        List<RefundProductInfo> refundProductInfo = orderEntity.getRefundProductInfoList();
        CustomerEntity customerEntity = orderEntity.getCustomerEntity();
        FranchiseeEntity franchiseeEntity = orderEntity.getFranchiseeEntity();

        log.trace("***************************************************");
        log.trace(" @@ orderEntity = {}", orderEntity.getTotalRefund());
        log.trace(" @@ orderEntity = {}", orderEntity.getTotalRefund());
        log.trace("***************************************************");

        RefundApproveRequestBuilder refundApproveRequestBuilder = RefundApproveRequest.builder()
                .serviceName(CustomValue.APPLICATION_CODE)
                .nationality(customerEntity.getNation())
                .totalAmount(orderEntity.getTotalAmount())
                .businessNumber(franchiseeEntity.getBusinessNumber())
                .franchiseeName(franchiseeEntity.getStoreName())
                .franchiseeNumber(franchiseeEntity.getMemberNumber())
                .name(customerEntity.getCustomerName())
                .passportNumber(customerEntity.getPassportNumber())
                .indList("1")
                .indListNow("1")
                .productInfoList(refundProductInfo)
                .sellerName(franchiseeEntity.getSellerName())
                .storeAddress(franchiseeEntity.getStoreAddressBasic() + " " + franchiseeEntity.getStoreAddressDetail())
                .storeName(franchiseeEntity.getStoreName())
                .storeTel(franchiseeEntity.getStoreTel())
                .totalEdut("0")
                .totalIct("0")
                .totalQuantity(orderEntity.getTotalQuantity())
                .saleDate(orderEntity.getSaleDate())
                .totalRefund(orderEntity.getTotalRefund())
                .totalStr("0")
                .totalVat(orderEntity.getTotalVat());

        if (null != refundAfterDto) {
            RefundAfterBaseDto refundAfterInfo = refundAfterDto.getRefundAfterInfo();
            refundApproveRequestBuilder
                    .cusCode(refundAfterInfo.getCusCode())
                    .locaCode(refundAfterInfo.getLocaCode())
                    .purchaseSequenceNumber(refundAfterDto.getRefundItem().getDocId())
                    .kioskBsnmCode(refundAfterInfo.getKioskBsnmCode())
                    .kioskCode(refundAfterInfo.getKioskCode())
                    .cityRefundCenterCode(refundAfterInfo.getCounterTypeCode())
                    .manualYn(booleanToFixedTypeString(MANUAL == refundAfterInfo.getRefundAfterMethod()))
                    .cityYn(booleanToFixedTypeString(CITY == refundAfterInfo.getRefundAfterMethod()))
                    .kioskYn(booleanToFixedTypeString(KIOSK == refundAfterInfo.getRefundAfterMethod()))
                    .retryYn(booleanToFixedTypeString(refundAfterInfo.isRetry()));

            if (isVan) {
                refundApproveRequestBuilder
                        .afilYn("0");
            } else {
                refundApproveRequestBuilder
                        .afilYn("2");
            }
        }

        return refundApproveRequestBuilder.build();
    }

    private static String booleanToFixedTypeString(boolean booleanInfo) {
        return booleanInfo ? "1" : "0";
    }

    public static RefundApproveRequest of(OrderEntity orderEntity) {
        return of(orderEntity, null);
    }

    public static RefundApproveRequest of(OrderEntity orderEntity, RefundAfterDto.Request refundAfterDto) {
        return of(orderEntity, refundAfterDto, false);
    }
}
