package com.tpay.domains.point.domain;

import com.tpay.domains.point.application.dto.AdminPointInfo;
import com.tpay.domains.point.application.dto.WithdrawalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepositoryCustom{

    Page<AdminPointInfo> findPointsAdmin(Pageable pageRequest, WithdrawalStatus withdrawalStatus, String searchKeyword, boolean isBusinessNumber, Boolean isAll);
}
