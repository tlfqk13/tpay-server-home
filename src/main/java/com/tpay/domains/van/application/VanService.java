package com.tpay.domains.van.application;

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

    @Transactional
    public void createRefundAfter(String encryptPassportNumber, VanRefundAfterBaseDto refundAfterBaseDto) {
        CustomerEntity customerEntity = customerRepository.findByPassportNumber(encryptPassportNumber)
                .orElseThrow(NullPointerException::new);

        List<OrderEntity> orders = orderRepository.findOrders(customerEntity.getId());
        for (OrderEntity order : orders) {
            if(null != order.getRefundEntity()) {
                continue;
            }

            RefundAfterDto.Request refundAfterDto = RefundAfterDto.Request.of(refundAfterBaseDto, null);

            RefundEntity refundEntity =
                    refundService.save(
                            "",
                            "",
                            order);

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

    public VanOrdersDto.Response findVanOrder(String encryptedPassportNumber) {
        List<OrdersDtoInterface> ordersDtoInterfaceList = orderRepository.findVanOrdersDetail(encryptedPassportNumber);

        List<VanOrderDetail> baseList = new ArrayList<>();
        for (OrdersDtoInterface orderDto : ordersDtoInterfaceList) {
            baseList.add(VanOrderDetail.builder()
                    .docId(orderDto.getDocId())
                    .shopNm(orderDto.getShopNm())
                    .shopTypeCcd(orderDto.getShopTypeCcd())
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

    private String checkRefundStatus(String customCleanceYn) {
        switch (customCleanceYn) {
            case "0":   // APPROVAL
                return "Y";
            case "1":  // REJECT
                return "C";
            case "4":  // PRE_APPROVAL
                return "N";
        }
        return "N";
    }
}
