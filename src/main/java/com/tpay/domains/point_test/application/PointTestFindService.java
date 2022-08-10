package com.tpay.domains.point_test.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.util.DisappearDate;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantFindService;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.franchisee_upload.application.FranchiseeBankFindService;
import com.tpay.domains.franchisee_upload.domain.FranchiseeBankEntity;
import com.tpay.domains.point.application.dto.*;
import com.tpay.domains.point.domain.PointEntity;
import com.tpay.domains.point.domain.PointRepository;
import com.tpay.domains.point.domain.PointStatus;
import com.tpay.domains.point_scheduled.domain.PointScheduledRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointTestFindService {

    private final PointRepository pointRepository;
    private final PointScheduledRepository pointScheduledRepository;
    private final FranchiseeBankFindService franchiseeBankFindService;
    private final FranchiseeApplicantFindService franchiseeApplicantFindService;

    public PointFindResponse findPoints(
        Long franchiseeIndex, Integer week, Integer month, Integer page, Integer size) {

        //common
        LocalDate endDate = LocalDate.now().plusDays(1);
        LocalDate startDate = week > 0 ? endDate.minusWeeks(week) : endDate.minusMonths(month);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdDate").descending());

        // 2022.06.14 기존 포인트 출금 하단에는 포인트 적립예정, 포인트 적립예정 취소, 포인트 적립, 포인트 출금 정보가 모두 취합되어있었으나, 금일 회의에 의하여
        // 포인트 적립, 출금 정보만 표기하도록 함. 추후 개선될 여지가 있어서 코드는 주석처리
        //pointScheduled
//        List<PointScheduledEntity> pointScheduledEntityList = pointScheduledRepository.findAllByFranchiseeEntityIdAndCreatedDateBetween(franchiseeIndex, startDate.atStartOfDay(), endDate.atStartOfDay(), pageRequest);
//        List<PointInfo> pointInfoList = pointScheduledEntityList.stream().map(PointInfo::new).collect(Collectors.toList());

        //points
        List<PointEntity> pointEntityList =
                pointRepository
                        .findAllByPointStatusInAndFranchiseeEntityIdAndCreatedDateBetween(
                                new ArrayList<>(List.of(PointStatus.WITHDRAW, PointStatus.COMPLETE,PointStatus.SAVE))
                                ,franchiseeIndex
                                ,startDate.atStartOfDay()
                                ,endDate.atStartOfDay()
                                ,pageRequest);
        List<PointInfo> pointInfoList = pointEntityList.stream().map(PointInfo::new).collect(Collectors.toList());
        //merge - sort - return
//        pointInfoList.addAll(pointInfoList1);
//        pointInfoList.sort(new PointComparator());
        return PointFindResponse.builder()
            .startDate(startDate.format(DateTimeFormatter.ofPattern("yyyy. MM. dd")))
            .endDate(endDate.minusDays(1).format(DateTimeFormatter.ofPattern("yyyy. MM. dd")))
            .pointInfoList(pointInfoList)
            .build();
    }

    // 전체 포인트 / 적립예정포인트 / 소멸예정포인트 한방 Native Query. 순수 Jpa로 Group By 구현이 어렵고 성능이슈에 따라 Native로 남겨둠
    public PointTotalResponseInterface findPointsTotal(Long franchiseeIndex) {
        LocalDateTime disappearDate = DisappearDate.getDisappearDate();
        return pointRepository.findPointsTotal(franchiseeIndex, disappearDate);
    }

    public AdminPointResponse findPointsAdmin(Boolean isAll, WithdrawalStatus withdrawalStatus,int page,String searchKeyword) {
        Page<PointEntity> result;
        List<Boolean> booleanList = new ArrayList<>(List.of(false));
        Pageable pageRequest = PageRequest.of(page,15);
        boolean isBusinessNumber = searchKeyword.chars().allMatch(Character::isDigit);

        if (isAll) {
            booleanList.add(true);
        }

        if(searchKeyword.isEmpty()){
            result = pointRepository.findByPointStatusInAndIsReadInOrderByIdDesc(withdrawalStatus.getPointStatusList(), booleanList,pageRequest);
        }else{
            if(isBusinessNumber){
                result = pointRepository.findByPointStatusInAndIsReadInAndFranchiseeEntityBusinessNumberContainingOrderByIdDesc(withdrawalStatus.getPointStatusList(),booleanList,pageRequest,searchKeyword);
            }else{
                result = pointRepository.findByPointStatusInAndIsReadInAndFranchiseeEntityStoreNameContainingOrderByIdDesc(withdrawalStatus.getPointStatusList(),booleanList,pageRequest,searchKeyword);
            }
        }

        List<AdminPointInfo> adminPointInfoList = result.stream().map(AdminPointInfo::new).collect(Collectors.toList());
        int totalPage = result.getTotalPages();
        if(totalPage != 0){
            totalPage = totalPage -1;
        }
        AdminPointResponse adminPointResponse = AdminPointResponse.builder()
                .adminPointInfoList(adminPointInfoList)
                .totalPage(totalPage)
                .build();

        return adminPointResponse;
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
            .currentPoint(pointEntity.getBalance() + pointEntity.getChange())
            .amount(pointEntity.getChange())
            .afterPayment(pointEntity.getBalance())
            .isReadTPoint(pointEntity.getIsRead())

            .bankName(franchiseeBankEntity.getBankName())
            .accountNumber(franchiseeBankEntity.getAccountNumber())
            .build();

    }

    private static class PointComparator implements Comparator<PointInfo> {
        @Override
        public int compare(PointInfo o1, PointInfo o2) {
            Long o2Long = Long.parseLong(o2.getDatetime().replaceAll("\\.", "").replaceAll(" ", ""));
            Long o1Long = Long.parseLong(o1.getDatetime().replaceAll("\\.", "").replaceAll(" ", ""));
            Long l = o2Long - o1Long;
            return l.intValue();
        }
    }
}
