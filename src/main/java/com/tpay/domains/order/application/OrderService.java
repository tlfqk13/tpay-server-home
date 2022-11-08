package com.tpay.domains.order.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.OrderNotFoundException;
import com.tpay.commons.util.converter.NumberFormatConverter;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.order.domain.OrderRepository;
import com.tpay.domains.vat.application.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

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

    public List<String> findCmsVatTotal(Long franchiseeIndex, LocalDate startLocalDate, LocalDate endLocalDate) {

      VatTotalDto.Response vatTotalResponse = orderRepository.findMonthlyTotal(franchiseeIndex, startLocalDate, endLocalDate);

      List<String> result = new ArrayList<>();
      result.add(NumberFormatConverter.addCommaToNumber(vatTotalResponse.getTotalCount()));
      result.add(NumberFormatConverter.addCommaToNumber(vatTotalResponse.getTotalAmount()));
      result.add(NumberFormatConverter.addCommaToNumber(vatTotalResponse.getTotalVat()));
      result.add(NumberFormatConverter.addCommaToNumber(vatTotalResponse.getTotalRefund()));
      result.add(NumberFormatConverter.addCommaToNumber(vatTotalResponse.getTotalCommission()));

      return result;
    }

    public List<List<String>> findCmsVatDetail(Long franchiseeIndex, LocalDate startLocalDate, LocalDate endLocalDate, boolean isPaging) {
        int pageData = 15;
        if(isPaging){
            pageData = 100;
        }

        List<VatDetailDto.Response> vatDetailResponse = orderRepository.findMonthlyCmsVatDetail(franchiseeIndex, startLocalDate, endLocalDate,pageData);
        List<List<String>> detailResult = new ArrayList<>();

        for (VatDetailDto.Response response : vatDetailResponse) {
            List<String> baseList = new ArrayList<>();
            baseList.add(response.getPurchaseSerialNumber());
            log.trace(" @@ response = {}", response.getPurchaseSerialNumber());
            baseList.add(String.valueOf(response.getSaleDate()));
            baseList.add(response.getTakeOutConfirmNumber());
            baseList.add(NumberFormatConverter.addCommaToNumber(response.getAmount()));
            baseList.add(NumberFormatConverter.addCommaToNumber(response.getVat()));
            baseList.add(NumberFormatConverter.addCommaToNumber(response.getRefundAmount()));
            baseList.add(response.getCustomerName());
            baseList.add(response.getCustomerNational());
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

    public OrderEntity findOrderByPurchaseSn(String purchaseSn) {
        return orderRepository.findByOrderNumber(purchaseSn)
                .orElseThrow(() -> new OrderNotFoundException(ExceptionState.ORDER_NOT_FOUND));
    }

    public Optional<OrderEntity> findOrderByPurchaseSnInApi(String purchaseSn) {
        return orderRepository.findByOrderNumber(purchaseSn);
    }
}
