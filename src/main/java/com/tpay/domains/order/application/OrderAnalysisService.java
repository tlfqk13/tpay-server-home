package com.tpay.domains.order.application;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.refund.domain.RefundStatus;
import com.tpay.domains.order.application.dto.OrderFindResponse;
import com.tpay.domains.order.application.dto.OrderGroupingResponse;
import com.tpay.domains.order.application.dto.OrderAnalysisResponse;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.order.domain.OrderRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderAnalysisService {

  private static final Logger logger = LogManager.getLogger(OrderAnalysisService.class);

  private final OrderRepository orderRepository;
  private final RefundRepository refundRepository;
  private final FranchiseeRepository franchiseeRepository;

  public List<OrderAnalysisResponse> findUnit(Long franchiseeIndex, String period) {

    LocalDateTime startDate = LocalDateTime.now().with(LocalTime.MIN);
    LocalDateTime endDate = LocalDateTime.now().with(LocalTime.MAX);

    if (period.equals("week")) {
      startDate = endDate.minusWeeks(1);
    } else if (period.equals("month")) {
      startDate = endDate.minusMonths(1);
    }
    // or day

    logger.debug("find startDate : {} ~ endDate : {}", startDate, endDate);

    //    List<SaleEntity> pageSaleEntityList =
    //        saleRepository.findAllByFranchiseeEntityIdAndCreatedDateBetween(
    //            franchiseeIndex, startDate, endDate, pageable);
    List<OrderEntity> orderEntityList =
        orderRepository.findAllByFranchiseeEntityIdAndCreatedDateBetween(
            franchiseeIndex, startDate, endDate);

    List<OrderGroupingResponse> orderGroupingResponseList = new ArrayList<>();
    for (OrderEntity orderEntity : orderEntityList) {
      RefundEntity refundEntity =
          refundRepository.findByOrderEntityIdAndRefundStatus(
              orderEntity.getId(), RefundStatus.APPROVAL);
      if (refundEntity != null) {
        orderGroupingResponseList.add(
            OrderGroupingResponse.builder()
                .saleDate(orderEntity.getSaleDate().substring(0, 8))
                .totalAmount(orderEntity.getTotalAmount())
                .totalRefund(refundEntity.getTotalRefund())
                .totalVAT(orderEntity.getTotalVat())
                .build());
      }
    }

    List<OrderAnalysisResponse> orderAnalysisResponse = new LinkedList<>();

    if (orderGroupingResponseList != null && orderGroupingResponseList.size() > 0) {
      orderGroupingResponseList.stream()
          .collect(Collectors.groupingBy(OrderGroupingResponse::getSaleDate))
          .forEach(
              (saleDate, saleGroupingResponses) -> {
                String totalAmount =
                    saleGroupingResponses.stream()
                        .map(sale -> Long.parseLong(sale.getTotalAmount()))
                        .reduce(Long::sum)
                        .get()
                        .toString();
                String totalRefund =
                    saleGroupingResponses.stream()
                        .map(sale -> Long.parseLong(sale.getTotalRefund()))
                        .reduce(Long::sum)
                        .get()
                        .toString();
                String totalVAT =
                    saleGroupingResponses.stream()
                        .map(sale -> Long.parseLong(sale.getTotalVAT()))
                        .reduce(Long::sum)
                        .get()
                        .toString();

                orderAnalysisResponse.add(
                    OrderAnalysisResponse.builder()
                        .saleDate(saleDate)
                        .totalAmount(totalAmount)
                        .totalRefund(totalRefund)
                        .totalVAT(totalVAT)
                        .saleCount(saleGroupingResponses.size())
                        .build());
              });
    }

    return orderAnalysisResponse;
  }

  public List<OrderAnalysisResponse> findPeriod(
      Long franchiseeIndex, String startDate, String endDate) {

    LocalDate localDateStart = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
    LocalDate localDateEnd = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

    logger.debug(
        "Convert to LocalDate StartDate : {}  ~ EndDate : {}", localDateStart, localDateEnd);

    LocalDateTime localDateTimeStart = LocalDateTime.of(localDateStart, LocalTime.MIN);
    LocalDateTime localDateTimeEnd = LocalDateTime.of(localDateEnd, LocalTime.MAX);

    logger.debug(
        "Convert to LocalDateTime StartDate : {} ~ EndDate : {} ",
        localDateTimeStart,
        localDateTimeEnd);

    List<OrderEntity> orderEntityList =
        orderRepository.findAllByFranchiseeEntityIdAndCreatedDateBetween(
            franchiseeIndex, localDateTimeStart, localDateTimeEnd);

    List<OrderGroupingResponse> orderGroupingResponseList =
        orderEntityList.stream()
            .map(
                saleEntity -> {
                  RefundEntity refundEntity =
                      refundRepository.findByOrderEntityIdAndRefundStatus(
                          saleEntity.getId(), RefundStatus.APPROVAL);
                  return OrderGroupingResponse.builder()
                      .saleDate(saleEntity.getSaleDate().substring(0, 8))
                      .totalAmount(saleEntity.getTotalAmount())
                      .totalRefund(refundEntity.getTotalRefund())
                      .totalVAT(saleEntity.getTotalVat())
                      .build();
                })
            .collect(Collectors.toList());

    List<OrderAnalysisResponse> orderAnalysisResponse = new LinkedList<>();

    orderGroupingResponseList.stream()
        .collect(Collectors.groupingBy(OrderGroupingResponse::getSaleDate))
        .forEach(
            (saleDate, saleGroupingResponses) -> {
              String totalAmount =
                  saleGroupingResponses.stream()
                      .map(sale -> Long.parseLong(sale.getTotalAmount()))
                      .reduce(Long::sum)
                      .get()
                      .toString();
              String totalRefund =
                  saleGroupingResponses.stream()
                      .map(sale -> Long.parseLong(sale.getTotalRefund()))
                      .reduce(Long::sum)
                      .get()
                      .toString();
              String totalVAT =
                  saleGroupingResponses.stream()
                      .map(sale -> Long.parseLong(sale.getTotalVAT()))
                      .reduce(Long::sum)
                      .get()
                      .toString();

              orderAnalysisResponse.add(
                  OrderAnalysisResponse.builder()
                      .saleDate(saleDate)
                      .totalAmount(totalAmount)
                      .totalRefund(totalRefund)
                      .totalVAT(totalVAT)
                      .saleCount(saleGroupingResponses.size())
                      .build());
            });

    return orderAnalysisResponse;
  }

  public List<OrderFindResponse> findOneSale(Long franchiseeIndex, String saleDate) {
    FranchiseeEntity franchiseeEntity =
        franchiseeRepository
            .findById(franchiseeIndex)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Franchisee Index"));

    List<OrderEntity> orderEntityList =
        orderRepository.findByFranchiseeEntityAndSaleDateContaining(franchiseeEntity, saleDate);

    System.out.println(saleDate + "saleDate????????");
    System.out.println(orderEntityList.isEmpty() + "isEmpty??????");

    List<OrderFindResponse> orderFindResponseList = new ArrayList<>();
    for(OrderEntity orderEntity : orderEntityList) {
      RefundEntity refundEntity =
          refundRepository.findByOrderEntityIdAndRefundStatus(
              orderEntity.getId(), RefundStatus.APPROVAL);
      if(refundEntity != null) {
        orderFindResponseList.add(OrderFindResponse.builder()
            .saleId(orderEntity.getId())
            .totalRefund(refundEntity.getTotalRefund())
            .saleDate(orderEntity.getSaleDate())
            .orderNumber(orderEntity.getOrderNumber())
            .build());
      }
    }
    return orderFindResponseList;
  }
}
