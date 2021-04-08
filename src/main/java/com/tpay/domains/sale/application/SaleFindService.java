package com.tpay.domains.sale.application;

import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import com.tpay.domains.refund.application.dto.RefundInquiryRequest;
import com.tpay.domains.sale.application.dto.SaleFindResponse;
import com.tpay.domains.sale.domain.SaleEntity;
import com.tpay.domains.sale.domain.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SaleFindService {
  private final SaleRepository saleRepository;
  private final CustomerRepository customerRepository;

  public List<SaleFindResponse> findAllSale(RefundInquiryRequest refundInquiryRequest) {
    CustomerEntity customerEntity =
        customerRepository
            .findByCustomerNameAndPassportNumber(
                refundInquiryRequest.getName(), refundInquiryRequest.getPassportNumber())
            .orElseThrow(() -> new IllegalArgumentException("Invalid Customer"));
    List<SaleEntity> saleEntityList =
        saleRepository.findAllByCustomerEntityId(customerEntity.getId());
    List<SaleFindResponse> saleFindResponseList = new LinkedList<>();
    saleEntityList.stream()
        .forEach(
            saleEntity ->
                saleFindResponseList.add(
                    SaleFindResponse.builder()
                        .orderNumber(saleEntity.getOrderNumber())
                        .saleDate(saleEntity.getSaleDate())
                        .totalRefund(saleEntity.getTotalVat())
                        .build()));

    return saleFindResponseList;
  }
}
