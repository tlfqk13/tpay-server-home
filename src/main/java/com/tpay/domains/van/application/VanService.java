package com.tpay.domains.van.application;

import com.tpay.commons.aria.PassportNumberEncryptService;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.CustomerNotFoundException;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import com.tpay.domains.order.application.dto.OrdersDtoInterface;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.order.domain.OrderRepository;
import com.tpay.domains.refund.application.RefundService;
import com.tpay.domains.refund.domain.RefundAfterEntity;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund_core.application.dto.RefundAfterBaseDto;
import com.tpay.domains.refund_core.application.dto.RefundAfterDto;
import com.tpay.domains.van.domain.dto.VanOrderDetail;
import com.tpay.domains.van.domain.dto.VanOrdersDto;
import com.tpay.domains.van.domain.dto.VanRefundAfterBaseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VanService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    private final RefundService refundService;
    private final PassportNumberEncryptService encryptService;

    @Transactional
    public void createRefundAfter(String encryptPassportNumber, VanRefundAfterBaseDto refundAfterBaseDto, boolean isPassportMapping) {

        String encryptNumber = encryptService.encrypt(encryptPassportNumber);

        CustomerEntity customerEntity = customerRepository.findByPassportNumber(encryptNumber)
                .orElseThrow(() -> new CustomerNotFoundException(ExceptionState.CUSTOMER_NOT_FOUND));

        List<OrderEntity> orders;

        if (!isPassportMapping) {
            orders = orderRepository.findOrders(customerEntity.getId());
        } else {
            orders = orderRepository.findOrdersPassportMapping(refundAfterBaseDto.getBarcode());
        }
        for (OrderEntity order : orders) {
            if (null != order.getRefundEntity()) {
                continue;
            }

            RefundAfterDto.Request refundAfterDto = RefundAfterDto.Request.of(refundAfterBaseDto, null);

            RefundEntity refundEntity =
                    refundService.save(
                            "",
                            "",
                            order);

            if(null == order.getCustomerEntity()){
                order.updateCustomer(customerEntity);
            }

            RefundAfterBaseDto refundAfterInfo = refundAfterDto.getRefundAfterInfo();
            RefundAfterEntity refundAfterEntity = RefundAfterEntity.builder()
                    .cusCode(refundAfterInfo.getCusCode())
                    .locaCode(refundAfterInfo.getLocaCode())
                    .kioskBsnmCode(refundAfterBaseDto.getKioskBsnmCode())
                    .kioskCode(refundAfterInfo.getKioskCode())
                    .cityRefundCenterCode("")
                    .refundAfterMethod(refundAfterInfo.getRefundAfterMethod())
                    .build();
            refundEntity.addRefundAfterEntity(refundAfterEntity);
        }
    }

    public VanOrdersDto.Response findVanOrder(String encryptedPassportNumber, boolean isPassportMapping, String barcode) {

        String encryptNumber = encryptService.encrypt(encryptedPassportNumber);
        List<OrdersDtoInterface> ordersDtoInterfaceList;
        if(isPassportMapping){
            ordersDtoInterfaceList = orderRepository.findVanOrdersDetail(encryptNumber,barcode);
        }else{
            ordersDtoInterfaceList = orderRepository.findVanOrdersDetail(encryptNumber);
        }


        List<VanOrderDetail> baseList = new ArrayList<>();
        for (OrdersDtoInterface orderDto : ordersDtoInterfaceList) {
            int shopTypeCcd = 0;
            if (orderDto.getShopTypeCcd().equals("호텔")) {
                shopTypeCcd = 2;
            } else if (orderDto.getShopTypeCcd().equals("의료")) {
                shopTypeCcd = 3;
            } else {
                shopTypeCcd = 1;
            }
            baseList.add(VanOrderDetail.builder()
                    .docId(orderDto.getDocId())
                    .shopNm(orderDto.getShopNm())
                    .shopTypeCcd(shopTypeCcd)
                    .purchsDate(orderDto.getPurchsDate())
                    .totPurchsAmt(orderDto.getTotPurchsAmt())
                    .vat(orderDto.getVat())
                    .totalRefund(orderDto.getTotalRefund())
//                    .rfndAvailableYn(orderDto.getRfndAvailableYn())
                    .rfndAvailableYn("Y")
                    .earlyRfndYn(checkCityRefund(orderDto.getEarlyRfndYn()))
                    .customsCleanceYn(checkRefundStatus(orderDto.getCustomsCleanceYn()))
                    .build());
        }
        return VanOrdersDto.Response.builder().vanOrderDetails(baseList).build();
    }

    private String checkRefundAvailable() {
        // TODO: 환급 가능 확인 로직 추가해야함.
        return "Y";
    }

    private String checkCityRefund(String earlyRefundYn) {
        if (earlyRefundYn.equals("CITY")) {
            return "Y";
        }
        return "N";
    }

    // 2022/12/23 반출 , 미반출 , 반출거절
    //  해당 사항에 대해 반출거절 코드(* 반출거절 : C) 로 내려주시면 유인창구에서 수기 반출 확인 이후 환급 진행합니다.

    private String checkRefundStatus(String customCleanceYn) {
        switch (customCleanceYn) {
            case "0":   // APPROVAL
                return "Y";
            case "1":  // REJECT
                return "N";
            case "4":  // PRE_APPROVAL
                return "C";
        }
        return "N";
    }
}
