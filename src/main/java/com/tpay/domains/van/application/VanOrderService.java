package com.tpay.domains.van.application;

import com.tpay.domains.order.application.dto.OrdersDtoInterface;
import com.tpay.domains.order.domain.OrderRepository;
import com.tpay.domains.van.domain.dto.VanOrderDetail;
import com.tpay.domains.van.domain.dto.VanOrdersDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VanOrderService {

    private final OrderRepository orderRepository;

    public VanOrdersDto.Response findVanOrder(String encryptedPassportNumber) {
        List<OrdersDtoInterface> ordersDtoInterfaceList =  orderRepository.findOrdersDetail(encryptedPassportNumber);

        List<VanOrderDetail> baseList = new ArrayList<>();
        for (OrdersDtoInterface orderDto : ordersDtoInterfaceList) {
            baseList.add(VanOrderDetail.builder()
                    .docId(orderDto.getDocId())
                    .shopNm(orderDto.getShopNm())
                    .shopTypeCcd(orderDto.getShopTypeCcd())
                    .purchsDate(orderDto.getPurchsDate())
                    .totPurchsAmt(orderDto.getTotPurchsAmt())
                    .vat(orderDto.getVat())
                    //.rfndAvailableYn(orderDto.getRfndAvailableYn())
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
        if(earlyRefundYn.equals("CITY")) {
            return "Y";
        }
        return "N";
    }

    private String checkRefundStatus(String customCleanceYn) {
        if(customCleanceYn.equals("0")) {  // APPROVAL
            return "Y";
        } else if (customCleanceYn.equals("1")) { // REJECT
            return "C";
        }
        return "N";
    }
}
