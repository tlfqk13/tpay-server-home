package com.tpay.domains.refund.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import com.tpay.domains.product.domain.ProductEntity;
import com.tpay.domains.product.domain.ProductRepository;
import com.tpay.domains.refund.application.dto.RefundApprovalRequest;
import com.tpay.domains.refund.application.dto.RefundRegisterRequest;
import com.tpay.domains.refund.application.dto.RefundRequestProductInfoList;
import com.tpay.domains.refund.application.dto.RefundResponse;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.refund.domain.RefundStatus;
import com.tpay.domains.sale.domain.SaleEntity;
import com.tpay.domains.sale.domain.SaleLineEntity;
import com.tpay.domains.sale.domain.SaleLineRepository;
import com.tpay.domains.sale.domain.SaleRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RefundApprovalService {

  WebClient webClient = WebClient.builder().baseUrl(CustomValue.REFUND_SERVER).build();

  private final RefundRepository refundRepository;
  private final FranchiseeRepository franchiseeRepository;
  private final SaleLineRepository saleLineRepository;
  private final ProductRepository productRepository;
  private final SaleRepository saleRepository;
  private final CustomerRepository customerRepository;

  @Transactional
  public RefundResponse refundApproval(RefundRegisterRequest refundRegisterRequest) {
    FranchiseeEntity franchiseeEntity =
        franchiseeRepository
            .findById(refundRegisterRequest.getFranchiseeIndex())
            .orElseThrow(() -> new IllegalArgumentException("Invalid FranchiseeIndex"));
    CustomerEntity customerEntity =
        customerRepository
            .findById(refundRegisterRequest.getCustomerIndex())
            .orElseThrow(() -> new IllegalArgumentException("Invalid CustomerIndex"));

    ProductEntity productEntity =
        productRepository.save(
            ProductEntity.builder()
                .name(franchiseeEntity.getProductCategory())
                .code("001")
                .lineNumber("100")
                .price(refundRegisterRequest.getPrice())
                .build());

    List<SaleLineEntity> saleLineEntityList = new LinkedList<>();
    saleLineEntityList.add(
        SaleLineEntity.builder().productEntity(productEntity).quantity("1").build());

    SaleEntity saleEntity =
        saleRepository.save(
            SaleEntity.builder()
                .saleLineEntity(saleLineEntityList)
                .franchiseeEntity(franchiseeEntity)
                .customerEntity(customerEntity)
                .build());

    saleLineRepository.save(
        SaleLineEntity.builder()
            .quantity("1")
            .saleEntity(saleEntity)
            .productEntity(productEntity)
            .build());

    List<RefundRequestProductInfoList> refundRequestProductInfoList =
        initRefundProductInfoList(saleLineEntityList);

    RefundApprovalRequest refundApprovalRequest =
        initRefundApproval(
            customerEntity, saleEntity, franchiseeEntity, refundRequestProductInfoList);

    System.out.println("==========" + refundApprovalRequest);
    RefundResponse refundResponse =
        webClient
            .post()
            .uri("/refund/approval")
            .bodyValue(refundApprovalRequest)
            .exchangeToMono(clientResponse -> clientResponse.bodyToMono(RefundResponse.class))
            .block();

    System.out.println("RefundCode ????????? " + refundResponse.getResponseCode());
    RefundStatus refundStatus =
        refundResponse.getResponseCode().equals("0000")
            ? RefundStatus.APPROVAL
            : RefundStatus.REJECT;
    System.out.println(refundStatus);
    refundRepository.save(
        RefundEntity.builder()
            .refundStatus(refundStatus)
            .saleEntity(saleEntity)
            .totalRefund(calTotalRefund(saleEntity.getTotalAmount()))
            .approvalNumber(refundResponse.getTakeoutNumber())
            .build());

    return refundResponse;
  }

  public String calTotalRefund(String amount) {
    double amt = Double.parseDouble(amount);
    int calRefund = (int) Math.floor(amt * 7) / 100;
    return Integer.toString(calRefund);
  }

  public RefundApprovalRequest initRefundApproval(
      CustomerEntity customerEntity,
      SaleEntity saleEntity,
      FranchiseeEntity franchiseeEntity,
      List<RefundRequestProductInfoList> refundRequestProductInfoList) {
    return RefundApprovalRequest.builder()
        .nationality(customerEntity.getNation())
        .amount(saleEntity.getTotalAmount())
        .businessNumber(franchiseeEntity.getBusinessNumber())
        .franchiseeName(franchiseeEntity.getStoreName())
        .franchiseeNumber(franchiseeEntity.getMemberNumber())
        .name(customerEntity.getCustomerName())
        .orderNumber(saleEntity.getOrderNumber())
        .passportNumber(customerEntity.getPassportNumber())
        .productList("1")
        .productListNow("1")
        .refundRequestProductInfoList(refundRequestProductInfoList)
        .sellerName(franchiseeEntity.getSellerName())
        .storeAddress(franchiseeEntity.getStoreAddress())
        .storeName(franchiseeEntity.getStoreName())
        .storeTel(franchiseeEntity.getStoreTel())
        .totalEdut("0")
        .totalIct("0")
        .totalQuantity(saleEntity.getTotalQuantity())
        .totalRefund(calTotalRefund(saleEntity.getTotalAmount()))
        .totalStr("0")
        .totalVAT(saleEntity.getTotalVat())
        .build();
  }

  public List<RefundRequestProductInfoList> initRefundProductInfoList(
      List<SaleLineEntity> saleLineEntity) {
    List<RefundRequestProductInfoList> refundRequestProductInfoList = new LinkedList<>();

    for (SaleLineEntity sale : saleLineEntity) {

      refundRequestProductInfoList.add(
          RefundRequestProductInfoList.builder()
              .productSequenceNumber(sale.getProductEntity().getLineNumber())
              .productQuantity(sale.getQuantity())
              .productPrice(sale.getProductEntity().getPrice())
              .productName(sale.getProductEntity().getName())
              .productCode(sale.getProductEntity().getCode())
              .salePrice(sale.getTotalPrice())
              .indStr("0")
              .indVAT(sale.getVat())
              .indIct("0")
              .indEdut("0")
              .build());
    }

    return refundRequestProductInfoList;
  }
}
