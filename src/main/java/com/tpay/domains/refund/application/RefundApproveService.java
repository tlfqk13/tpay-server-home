package com.tpay.domains.refund.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.domains.customer.application.CustomerFindService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.order.application.OrderSaveService;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.point.domain.PointEntity;
import com.tpay.domains.point.domain.PointRepository;
import com.tpay.domains.point.domain.PointStatus;
import com.tpay.domains.point.domain.SignType;
import com.tpay.domains.product.application.ProductFindService;
import com.tpay.domains.product.domain.ProductEntity;
import com.tpay.domains.refund.application.dto.RefundApprovalRequest;
import com.tpay.domains.refund.application.dto.RefundSaveRequest;
import com.tpay.domains.refund.application.dto.RefundProductInfo;
import com.tpay.domains.refund.application.dto.RefundResponse;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.refund.domain.RefundStatus;
import java.time.LocalDateTime;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class RefundApproveService {

  private final FranchiseeFindService franchiseeFindService;
  private final CustomerFindService customerFindService;
  private final ProductFindService productFindService;
  private final OrderSaveService orderSaveService;
  private final WebClient.Builder builder;

  private final RefundRepository refundRepository;
  private final PointRepository pointRepository;

  @Transactional
  public RefundResponse refundApproval(RefundSaveRequest request) {
    FranchiseeEntity franchiseeEntity =
        franchiseeFindService.findByIndex(request.getFranchiseeIndex());

    CustomerEntity customerEntity = customerFindService.findByIndex(request.getCustomerIndex());

    ProductEntity productEntity =
        productFindService.findOrElseSave(
            franchiseeEntity.getProductCategory(), request.getPrice());

    OrderEntity orderEntity =
        orderSaveService.save(franchiseeEntity, customerEntity, productEntity);

    List<RefundProductInfo> refundProductInfo = orderEntity.getRefundProductInfoList();

    RefundApprovalRequest refundApprovalRequest =
        initRefundApproval(customerEntity, orderEntity, franchiseeEntity, refundProductInfo);

    WebClient webClient = builder.build();
    String uri = CustomValue.REFUND_SERVER + "/reufnd/approval";
    RefundResponse refundResponse =
        webClient
            .post()
            .uri(uri)
            .bodyValue(refundApprovalRequest)
            .retrieve()
            .bodyToMono(RefundResponse.class)
            //.exchangeToMono(clientResponse -> clientResponse.bodyToMono(RefundResponse.class))
            .block();

    RefundStatus refundStatus =
        refundResponse.getResponseCode().equals("0000")
            ? RefundStatus.APPROVAL
            : RefundStatus.REJECT;

    refundRepository.save(
        RefundEntity.builder()
            .refundStatus(refundStatus)
            .orderEntity(orderEntity)
            .totalRefund(calTotalRefund(orderEntity.getTotalAmount()))
            .approvalNumber(refundResponse.getTakeoutNumber())
            .build());

    long point = (long) Math.floor(Double.parseDouble(orderEntity.getTotalAmount()) * 7) / 100;
    franchiseeEntity.changeBalance(SignType.POSITIVE, point);
    PointEntity pointEntity =
        PointEntity.builder()
            .createdDate(LocalDateTime.now())
            .signType(SignType.POSITIVE)
            .change(point)
            .pointStatus(PointStatus.SAVE)
            .balance(franchiseeEntity.getBalance())
            .franchiseeEntity(franchiseeEntity)
            .build();
    pointRepository.save(pointEntity);

    return refundResponse;
  }

  public String calTotalRefund(String amount) {
    double amt = Double.parseDouble(amount);
    int calRefund = (int) Math.floor(amt * 7) / 100;
    return Integer.toString(calRefund);
  }

  public RefundApprovalRequest initRefundApproval(
      CustomerEntity customerEntity,
      OrderEntity orderEntity,
      FranchiseeEntity franchiseeEntity,
      List<RefundProductInfo> refundProductInfo) {
    return RefundApprovalRequest.builder()
        .nationality(customerEntity.getNation())
        .amount(orderEntity.getTotalAmount())
        .businessNumber(franchiseeEntity.getBusinessNumber())
        .franchiseeName(franchiseeEntity.getStoreName())
        .franchiseeNumber(franchiseeEntity.getMemberNumber())
        .name(customerEntity.getCustomerName())
        .orderNumber(orderEntity.getOrderNumber())
        .passportNumber(customerEntity.getPassportNumber())
        .productList("1")
        .productListNow("1")
        .refundProductInfo(refundProductInfo)
        .sellerName(franchiseeEntity.getSellerName())
        .storeAddress(franchiseeEntity.getStoreAddress())
        .storeName(franchiseeEntity.getStoreName())
        .storeTel(franchiseeEntity.getStoreTel())
        .totalEdut("0")
        .totalIct("0")
        .totalQuantity(orderEntity.getTotalQuantity())
        .totalRefund(calTotalRefund(orderEntity.getTotalAmount()))
        .totalStr("0")
        .totalVAT(orderEntity.getTotalVat())
        .build();
  }

  @Transactional
  public RefundResponse approve(RefundSaveRequest request) {
    return null;
  }
}
