package com.tpay.domains.point.application.dto;


import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.point.domain.PointEntity;
import com.tpay.domains.point.domain.PointStatus;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AdminPointResponse {

    private int totalPage;
    private List<AdminPointInfo> adminPointInfoList;
}
