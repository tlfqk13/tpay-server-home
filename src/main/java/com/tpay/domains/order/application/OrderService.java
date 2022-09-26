package com.tpay.domains.order.application;

import com.tpay.commons.aria.PassportNumberEncryptService;
import com.tpay.commons.util.converter.NumberFormatConverter;
import com.tpay.domains.order.application.dto.OrderDtoInfo;
import com.tpay.domains.order.application.dto.OrdersDto;
import com.tpay.domains.order.application.dto.OrdersDtoInterface;
import com.tpay.domains.order.domain.OrderRepository;
import com.tpay.domains.vat.application.dto.VatDetailResponseInterface;
import com.tpay.domains.vat.application.dto.VatReportResponseInterface;
import com.tpay.domains.vat.application.dto.VatResponse;
import com.tpay.domains.vat.application.dto.VatTotalResponseInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final PassportNumberEncryptService passportNumberEncryptService;

    public VatResponse findQuarterlyVatReport(Long franchiseeIndex, LocalDate startDate, LocalDate endDate) {

        VatReportResponseInterface queryResult = orderRepository.findQuarterlyVatReport(franchiseeIndex, startDate, endDate);
        if (queryResult == null) {
            return new VatResponse(true);
        }

        return VatResponse.builder()
                .totalAmount(queryResult.getTotalAmount())
                .totalCount(queryResult.getTotalCount())
                .totalVat(queryResult.getTotalVat())
                .totalSupply(queryResult.getTotalSupply())
                .build();

    }

    public List<String> findQuarterlyTotal(Long franchiseeIndex, LocalDate startDate, LocalDate endDate) {
        VatTotalResponseInterface vatTotalResponseInterface = orderRepository.findQuarterlyTotal(franchiseeIndex, startDate, endDate);
        List<String> result = new ArrayList<>();
        result.add(NumberFormatConverter.addCommaToNumber(vatTotalResponseInterface.getTotalCount()));
        result.add(NumberFormatConverter.addCommaToNumber(vatTotalResponseInterface.getTotalAmount()));
        result.add(NumberFormatConverter.addCommaToNumber(vatTotalResponseInterface.getTotalVat()));
        result.add(NumberFormatConverter.addCommaToNumber(vatTotalResponseInterface.getTotalRefund()));
        return result;
    }

    public List<String> findMonthlyTotal(Long franchiseeIndex, String year, String month) {
        VatTotalResponseInterface vatTotalResponseInterface = orderRepository.findMonthlyTotal(franchiseeIndex, year, month);
        List<String> result = new ArrayList<>();
        result.add(NumberFormatConverter.addCommaToNumber(vatTotalResponseInterface.getTotalCount()));
        result.add(NumberFormatConverter.addCommaToNumber(vatTotalResponseInterface.getTotalAmount()));
        result.add(NumberFormatConverter.addCommaToNumber(vatTotalResponseInterface.getTotalVat()));
        result.add(NumberFormatConverter.addCommaToNumber(vatTotalResponseInterface.getTotalRefund()));
        result.add(NumberFormatConverter.addCommaToNumber(vatTotalResponseInterface.getTotalCommission()));
        return result;
    }

    public List<List<String>> findQuarterlyDetail(Long franchiseeIndex, LocalDate startDate, LocalDate endDate) {
        List<VatDetailResponseInterface> vatDetailResponseInterfaceList
                = orderRepository.findQuarterlyVatDetail(franchiseeIndex, startDate, endDate);

        List<List<String>> detailResult = new ArrayList<>();
        for (VatDetailResponseInterface vatDetailResponseInterface : vatDetailResponseInterfaceList) {
            List<String> baseList = new ArrayList<>();
            baseList.add(vatDetailResponseInterface.getPurchaseSerialNumber());
            baseList.add(vatDetailResponseInterface.getSaleDate());
            baseList.add(vatDetailResponseInterface.getTakeoutConfirmNumber());
            baseList.add(NumberFormatConverter.addCommaToNumber(vatDetailResponseInterface.getAmount()));
            baseList.add(NumberFormatConverter.addCommaToNumber(vatDetailResponseInterface.getVat()));
            baseList.add(NumberFormatConverter.addCommaToNumber(vatDetailResponseInterface.getRefundAmount()));
            baseList.add(vatDetailResponseInterface.getCustomerName());
            baseList.add(vatDetailResponseInterface.getCustomerNational());
            detailResult.add(baseList);
        }
        return detailResult;
    }

    public List<List<String>> findMonthlyDetail(Long franchiseeIndex, String year, String month) {
        List<VatDetailResponseInterface> vatDetailResponseInterfaceList = orderRepository.findMonthlyVatDetail(franchiseeIndex, year, month);
        List<List<String>> detailResult = new ArrayList<>();
        for (VatDetailResponseInterface vatDetailResponseInterface : vatDetailResponseInterfaceList) {
            List<String> baseList = new ArrayList<>();
            baseList.add(vatDetailResponseInterface.getPurchaseSerialNumber());
            baseList.add(vatDetailResponseInterface.getSaleDate());
            baseList.add(vatDetailResponseInterface.getTakeoutConfirmNumber());
            baseList.add(NumberFormatConverter.addCommaToNumber(vatDetailResponseInterface.getAmount()));
            baseList.add(NumberFormatConverter.addCommaToNumber(vatDetailResponseInterface.getVat()));
            baseList.add(NumberFormatConverter.addCommaToNumber(vatDetailResponseInterface.getRefundAmount()));
            baseList.add(vatDetailResponseInterface.getCustomerName());
            baseList.add(vatDetailResponseInterface.getCustomerNational());
            detailResult.add(baseList);
        }
        return detailResult;
    }

    public List<List<String>> findMonthlyCmsDetail(Long franchiseeIndex, String year, String month,boolean isPaging) {
        int pageData = 15;
        if(isPaging){
            pageData = 100;
        }
        List<VatDetailResponseInterface> vatDetailResponseInterfaceList = orderRepository.findMonthlyCmsDetail(franchiseeIndex, year, month,pageData);
        List<List<String>> detailResult = new ArrayList<>();
        for (VatDetailResponseInterface vatDetailResponseInterface : vatDetailResponseInterfaceList) {
            List<String> baseList = new ArrayList<>();
            baseList.add(vatDetailResponseInterface.getPurchaseSerialNumber());
            baseList.add(vatDetailResponseInterface.getSaleDate());
            baseList.add(vatDetailResponseInterface.getTakeoutConfirmNumber());
            baseList.add(NumberFormatConverter.addCommaToNumber(vatDetailResponseInterface.getAmount()));
            baseList.add(NumberFormatConverter.addCommaToNumber(vatDetailResponseInterface.getVat()));
            baseList.add(NumberFormatConverter.addCommaToNumber(vatDetailResponseInterface.getRefundAmount()));
            baseList.add(vatDetailResponseInterface.getCustomerName());
            baseList.add(vatDetailResponseInterface.getCustomerNational());
            detailResult.add(baseList);
        }
        return detailResult;
    }

    @Transactional
    public Long sumTotalSaleAmountByFranchiseeIndex(Long franchiseeIndex) {
        Optional<Long> optionalResult = orderRepository.sumTotalSaleAmountByFranchiseeIndex(franchiseeIndex);
        if (optionalResult.isEmpty()) {
            return 0L;
        } else return optionalResult.get();
    }

    @Transactional
    public void deleteByIndex(Long orderIndex) {
        try {
            orderRepository.deleteById(orderIndex);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("삭제불가 orderIndex = {}", orderIndex);
        }
    }

    public List<VatDetailResponseInterface> findDetailBetweenDates(Long franchiseeIndex, LocalDate startDate, LocalDate endDate) {
        return orderRepository.findQuarterlyVatDetail(franchiseeIndex, startDate, endDate);
    }

    public VatTotalResponseInterface findTotalBetweenDates(Long franchiseeIndex, LocalDate startDate, LocalDate endDate) {
        return orderRepository.findQuarterlyTotal(franchiseeIndex, startDate, endDate);
    }

    public OrdersDto.Response ordersDetail(String passportNumber) {
        String encryptedPassportNumber = passportNumberEncryptService.encrypt(passportNumber);
        List<OrdersDtoInterface> ordersDtoInterfaceList =  orderRepository.findOrdersDetail(encryptedPassportNumber);

        List<OrderDtoInfo> baseList = new ArrayList<>();
        for (OrdersDtoInterface OrdersDtoInterface : ordersDtoInterfaceList) {
            baseList.add(OrderDtoInfo.builder()
                            .docId(OrdersDtoInterface.getDocId())
                            .shopNm(OrdersDtoInterface.getShopNm())
                            .shopTypeCcd(OrdersDtoInterface.getShopTypeCcd())
                            .purchsDate(OrdersDtoInterface.getPurchsDate())
                            .totPurchsAmt(OrdersDtoInterface.getTotPurchsAmt())
                            .vat(OrdersDtoInterface.getVat())
                            //.rfndAvailableYn(OrdersDtoInterface.getRfndAvailableYn())
                            .rfndAvailableYn("Y")
                            //.earlyRfndYn(OrdersDtoInterface.getEarlyRfndYn())
                            .earlyRfndYn("N")
                            //.customsCleanceYn(OrdersDtoInterface.getCustomsCleanceYn())
                            .customsCleanceYn("N")
                    .build());
        }
        return OrdersDto.Response.builder().ordersDtoList(baseList).build();
    }
}
