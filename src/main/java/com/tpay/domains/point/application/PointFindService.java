package com.tpay.domains.point.application;

import com.tpay.domains.batch.application.PointConfirmedService;
import com.tpay.domains.point.application.dto.PointFindResponse;
import com.tpay.domains.point.application.dto.PointInfo;
import com.tpay.domains.point.application.dto.PointTotalResponseInterface;
import com.tpay.domains.point.domain.PointEntity;
import com.tpay.domains.point.domain.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointFindService {

  private final PointRepository pointRepository;
  private final PointConfirmedService pointConfirmedService;

  public PointFindResponse findPoints(
      Long franchiseeIndex, Integer week, Integer month, Integer page, Integer size) {

    LocalDate endDate = LocalDate.now().plusDays(1);
    LocalDate startDate = week > 0 ? endDate.minusWeeks(week) : endDate.minusMonths(month);

    PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdDate").descending());

    List<PointEntity> pointEntityList =
        pointRepository.findAllByFranchiseeEntityIdAndCreatedDateBetween(
            franchiseeIndex, startDate.atStartOfDay(), endDate.atStartOfDay(), pageRequest);

    List<PointInfo> pointInfoList =
        pointEntityList.stream()
            .map(
                pointEntity -> {
                  String createdDateAsString =
                      pointEntity
                          .getCreatedDate()
                          .format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH.mm.ss"));
                  return PointInfo.builder()
                      .datetime(createdDateAsString)
                      .pointStatus(pointEntity.getPointStatus())
                      .totalAmount(pointEntity.getOrderEntity().getTotalAmount())
                      .value(pointEntity.getChange())
                      .build();
                })
            .collect(Collectors.toList());

    return PointFindResponse.builder()
        .startDate(startDate)
        .endDate(endDate)
        .pointInfoList(pointInfoList)
        .build();
  }

  public PointTotalResponseInterface findPointsTotal(Long franchiseeIndex) {
    LocalDate disappearDate = LocalDate.now().minusYears(5);
    LocalDate scheduledDate = LocalDate.now().minusWeeks(2);
    return pointRepository.findPointsTotal(franchiseeIndex, disappearDate, scheduledDate);
  }
}
