package com.tpay.domains.point.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.util.WithdrawalStatus;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantFindService;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.franchisee_upload.application.FranchiseeBankFindService;
import com.tpay.domains.franchisee_upload.domain.FranchiseeBankEntity;
import com.tpay.domains.point.application.dto.*;
import com.tpay.domains.point.domain.PointEntity;
import com.tpay.domains.point.domain.PointRepository;
import com.tpay.domains.point.domain.PointStatus;
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
  private final FranchiseeBankFindService franchiseeBankFindService;
  private final FranchiseeApplicantFindService franchiseeApplicantFindService;

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
// TODO: 2022/03/16 속도 개선을 위해서는 JPA 말고 네이티브 쿼리로
    List<PointEntity> pointEntityList = pointRepository.findAllByFranchiseeEntityIdAndCreatedDateBetweenAndPointStatus(franchiseeIndex, startDate.atStartOfDay(), endDate.atStartOfDay(), pageRequest, PointStatus.WITHDRAW);
    List<PointInfo> pointInfoList1 = pointEntityList.stream()
        .map(pointEntity -> {
          String createdDateAsString = pointEntity.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH.mm.ss"));
          return PointInfo.builder()
              .datetime(createdDateAsString)
              .pointStatus(pointEntity.getPointStatus())
              .value(pointEntity.getChange())
              .build();
        })
        .collect(Collectors.toList());

    pointInfoList.addAll(pointInfoList1);
    pointInfoList.sort(new PointComparator());
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

  public PointFindDetailResponse findDetailByIndex(Long pointsIndex) {
    PointEntity pointEntity = pointRepository.findById(pointsIndex).orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid PointsIndex"));
    FranchiseeEntity franchiseeEntity = pointEntity.getFranchiseeEntity();
    FranchiseeApplicantEntity franchiseeApplicantEntity = franchiseeApplicantFindService.findByFranchiseeEntity(franchiseeEntity);
    FranchiseeBankEntity franchiseeBankEntity = franchiseeBankFindService.findByFranchiseeEntity(franchiseeEntity);

    return PointFindDetailResponse.builder()
        .storeName(franchiseeEntity.getStoreName())
        .sellerName(franchiseeEntity.getSellerName())
        .businessNumber(franchiseeEntity.getBusinessNumber())
        .storeTel(franchiseeEntity.getStoreTel())
        .email(franchiseeEntity.getEmail())
        .isTaxRefundShop(franchiseeEntity.getIsTaxRefundShop())
        .franchiseeStatus(franchiseeApplicantEntity.getFranchiseeStatus())
        .signboard(franchiseeEntity.getSignboard())
        .productCategory(franchiseeEntity.getProductCategory())
        .storeNumber(franchiseeEntity.getStoreNumber())
        .storeAddressBasic(franchiseeEntity.getStoreAddressBasic())
        .storeAddressDetail(franchiseeEntity.getStoreAddressDetail())
        .createdDate(franchiseeEntity.getCreatedDate())
        .isRead(franchiseeApplicantEntity.getIsRead())

        .requestedDate(pointEntity.getCreatedDate())
        .pointStatus(pointEntity.getPointStatus())
        //역산해서 추출하는 것임
        .currentPoint(pointEntity.getBalance() + pointEntity.getChange())
        .amount(pointEntity.getChange())
        .afterPayment(pointEntity.getBalance())
        .isReadTPoint(pointEntity.getIsRead())

        .bankName(franchiseeBankEntity.getBankName())
        .accountNumber(franchiseeBankEntity.getAccountNumber())
        .build();

  }

}
