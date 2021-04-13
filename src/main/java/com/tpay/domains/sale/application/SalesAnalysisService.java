package com.tpay.domains.sale.application;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.refund.domain.RefundStatus;
import com.tpay.domains.sale.application.dto.SaleFindResponse;
import com.tpay.domains.sale.application.dto.SaleGroupingResponse;
import com.tpay.domains.sale.application.dto.SalesAnalysisResponse;
import com.tpay.domains.sale.domain.SaleEntity;
import com.tpay.domains.sale.domain.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SalesAnalysisService {

  private static final Logger logger = LogManager.getLogger(SalesAnalysisService.class);

  private final SaleRepository saleRepository;
  private final RefundRepository refundRepository;
  private final FranchiseeRepository franchiseeRepository;

  public List<SalesAnalysisResponse> findUnit(Long franchiseeIndex, String period) {

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
    List<SaleEntity> saleEntityList =
        saleRepository.findAllByFranchiseeEntityIdAndCreatedDateBetween(
            franchiseeIndex, startDate, endDate);

    List<SaleGroupingResponse> saleGroupingResponseList =
        saleEntityList.stream()
            .map(
                saleEntity -> {
                  RefundEntity refundEntity =
                      refundRepository.findBySaleEntityIdAndRefundStatus(
                          saleEntity.getId(), RefundStatus.APPROVAL);
                  return SaleGroupingResponse.builder()
                      .saleDate(saleEntity.getSaleDate().substring(0, 8))
                      .totalAmount(saleEntity.getTotalAmount())
                      .totalRefund(refundEntity.getTotalRefund())
                      .totalVAT(saleEntity.getTotalVat())
                      .build();
                })
            .collect(Collectors.toList());

    List<SalesAnalysisResponse> salesAnalysisResponse = new LinkedList<>();

    saleGroupingResponseList.stream()
        .collect(Collectors.groupingBy(SaleGroupingResponse::getSaleDate))
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

              salesAnalysisResponse.add(
                  SalesAnalysisResponse.builder()
                      .saleDate(saleDate)
                      .totalAmount(totalAmount)
                      .totalRefund(totalRefund)
                      .totalVAT(totalVAT)
                      .saleCount(saleGroupingResponses.size())
                      .build());
            });

    return salesAnalysisResponse;
  }

  public List<SalesAnalysisResponse> findPeriod(
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

    List<SaleEntity> saleEntityList =
        saleRepository.findAllByFranchiseeEntityIdAndCreatedDateBetween(
            franchiseeIndex, localDateTimeStart, localDateTimeEnd);

    List<SaleGroupingResponse> saleGroupingResponseList =
        saleEntityList.stream()
            .map(
                saleEntity -> {
                  RefundEntity refundEntity =
                      refundRepository.findBySaleEntityIdAndRefundStatus(
                          saleEntity.getId(), RefundStatus.APPROVAL);
                  return SaleGroupingResponse.builder()
                      .saleDate(saleEntity.getSaleDate().substring(0, 8))
                      .totalAmount(saleEntity.getTotalAmount())
                      .totalRefund(refundEntity.getTotalRefund())
                      .totalVAT(saleEntity.getTotalVat())
                      .build();
                })
            .collect(Collectors.toList());

    List<SalesAnalysisResponse> salesAnalysisResponse = new LinkedList<>();

    saleGroupingResponseList.stream()
        .collect(Collectors.groupingBy(SaleGroupingResponse::getSaleDate))
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

              salesAnalysisResponse.add(
                  SalesAnalysisResponse.builder()
                      .saleDate(saleDate)
                      .totalAmount(totalAmount)
                      .totalRefund(totalRefund)
                      .totalVAT(totalVAT)
                      .saleCount(saleGroupingResponses.size())
                      .build());
            });

    return salesAnalysisResponse;
  }

  public List<SaleFindResponse> findOneSale(Long franchiseeIndex, String saleDate) {
    FranchiseeEntity franchiseeEntity =
        franchiseeRepository
            .findById(franchiseeIndex)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Franchisee Index"));

    List<SaleEntity> saleEntityList =
        saleRepository.findByFranchiseeEntityAndSaleDateContaining(franchiseeEntity, saleDate);

    System.out.println(saleDate + "saleDate????????");
    System.out.println(saleEntityList.isEmpty() + "isEmpty??????");

    List<SaleFindResponse> saleFindResponseList =
        saleEntityList.stream()
            .map(
                saleEntity -> {
                  RefundEntity refundEntity =
                      refundRepository.findBySaleEntityIdAndRefundStatus(
                          saleEntity.getId(), RefundStatus.APPROVAL);
                  return SaleFindResponse.builder()
                      .saleId(saleEntity.getId())
                      .totalRefund(refundEntity.getTotalRefund())
                      .saleDate(saleEntity.getSaleDate())
                      .orderNumber(saleEntity.getOrderNumber())
                      .build();
                })
            .collect(Collectors.toList());
    return saleFindResponseList;
  }
}
