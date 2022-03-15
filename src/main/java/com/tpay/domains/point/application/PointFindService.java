package com.tpay.domains.point.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.util.WithdrawalStatus;
import com.tpay.domains.point.application.dto.AdminPointFindResponseInterface;
import com.tpay.domains.point.application.dto.PointFindResponse;
import com.tpay.domains.point.application.dto.PointInfo;
import com.tpay.domains.point.application.dto.PointTotalResponseInterface;
import com.tpay.domains.point.domain.PointRepository;
import com.tpay.domains.point_scheduled.domain.PointScheduledEntity;
import com.tpay.domains.point_scheduled.domain.PointScheduledRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static com.tpay.commons.util.WithdrawalStatus.*;

@Service
@RequiredArgsConstructor
public class PointFindService {

  private final PointRepository pointRepository;
  private final PointScheduledRepository pointScheduledRepository;

  public PointFindResponse findPoints(
      Long franchiseeIndex, Integer week, Integer month, Integer page, Integer size) {

    LocalDate endDate = LocalDate.now().plusDays(1);
    LocalDate startDate = week > 0 ? endDate.minusWeeks(week) : endDate.minusMonths(month);

    PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdDate").descending());

    List<PointScheduledEntity> pointScheduledEntityList =
        pointScheduledRepository.findAllByFranchiseeEntityIdAndCreatedDateBetween(
            franchiseeIndex, startDate.atStartOfDay(), endDate.atStartOfDay(), pageRequest);

    List<PointInfo> pointInfoList =
        pointScheduledEntityList.stream()
            .map(
                pointScheduledEntity -> {
                  String createdDateAsString =
                      pointScheduledEntity
                          .getCreatedDate()
                          .format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH.mm.ss"));
                  return PointInfo.builder()
                      .datetime(createdDateAsString)
                      .pointStatus(pointScheduledEntity.getPointStatus())
                      .totalAmount(pointScheduledEntity.getOrderEntity().getTotalAmount())
                      .value(pointScheduledEntity.getValue())
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
    return pointRepository.findPointsTotal(franchiseeIndex, disappearDate);
  }


  public List<AdminPointFindResponseInterface> findPointsAdmin(Boolean isAll, WithdrawalStatus withdrawalStatus) {
    List<AdminPointFindResponseInterface> pointFindResponseInterfaceList;
    if (isAll) {
      if (withdrawalStatus.equals(ALL)) {
        pointFindResponseInterfaceList = pointRepository.findPointsAdminAll();
      } else if (withdrawalStatus.equals(WITHDRAW)) {
        pointFindResponseInterfaceList = pointRepository.findPointsAdminWithdraw();
      } else if (withdrawalStatus.equals(COMPLETE)) {
        pointFindResponseInterfaceList = pointRepository.findPointsAdminComplete();
      } else {
        throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid Withdrawal Status");
      }
    } else {
      if (withdrawalStatus.equals(ALL)) {
        pointFindResponseInterfaceList = pointRepository.findPointsAdminIsReadFalse();
      } else if (withdrawalStatus.equals(WITHDRAW)) {
        pointFindResponseInterfaceList = pointRepository.findPointsAdminWithdrawIsReadFalse();
      } else if (withdrawalStatus.equals(COMPLETE)) {
        pointFindResponseInterfaceList = pointRepository.findPointsAdminCompleteIsReadFalse();
      } else {
        throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid Withdrawal Status");
      }
    }
    return pointFindResponseInterfaceList;
  }
}
