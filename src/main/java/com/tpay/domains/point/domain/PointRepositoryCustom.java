package com.tpay.domains.point.domain;

import com.tpay.domains.point.application.dto.AdminPointInfo;
import com.tpay.domains.point.application.dto.WithdrawalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface PointRepositoryCustom {

    Long findDisappearPoints(Long franchiseeId, LocalDateTime disappearDate);

    Long findScheduledPoints(Long franchiseeId);

    Long findTotalPoints(Long franchiseeId);
    Page<AdminPointInfo> findPointsAdmin(Pageable pageRequest, WithdrawalStatus withdrawalStatus, String searchKeyword, boolean isBusinessNumber, Boolean isAll);

}
