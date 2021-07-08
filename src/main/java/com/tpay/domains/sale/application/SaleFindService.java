package com.tpay.domains.sale.application;

import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import com.tpay.domains.refund.application.dto.RefundInquiryRequest;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.refund.domain.RefundStatus;
import com.tpay.domains.sale.application.dto.SaleFindResponse;
import com.tpay.domains.sale.domain.SaleEntity;
import com.tpay.domains.sale.domain.SaleRepository;
import java.util.LinkedList;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SaleFindService {
  private final SaleRepository saleRepository;
  private final CustomerRepository customerRepository;
  private final RefundRepository refundRepository;

  @Transactional
  public List<SaleFindResponse> findAllSale(RefundInquiryRequest refundInquiryRequest) {
    CustomerEntity customerEntity =
        customerRepository
            .findByCustomerNameAndPassportNumber(
                refundInquiryRequest.getName(), refundInquiryRequest.getPassportNumber())
            .orElseThrow(() -> new IllegalArgumentException("Invalid Customer"));

    List<SaleEntity> saleEntityList =
        saleRepository.findAllByCustomerEntityId(customerEntity.getId());

    List<SaleFindResponse> saleFindResponseList = new LinkedList<>();
    for (SaleEntity saleEntity : saleEntityList) {
      RefundEntity refundEntity =
          refundRepository.findBySaleEntityIdAndRefundStatus(
              saleEntity.getId(), RefundStatus.APPROVAL);

      if (refundEntity != null) {
        saleFindResponseList.add(
            SaleFindResponse.builder()
                .refundId(refundEntity.getId())
                .saleId(saleEntity.getId())
                .orderNumber(saleEntity.getOrderNumber())
                .saleDate(saleEntity.getSaleDate())
                .totalRefund(refundEntity.getTotalRefund())
                .build());
      }
    }

    return saleFindResponseList;
  }
}
