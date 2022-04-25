package com.tpay.domains.point.application.dto;

import com.tpay.domains.point.domain.PointStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public enum WithdrawalStatus {
    WITHDRAW(new ArrayList<>(List.of(PointStatus.WITHDRAW))),
    COMPLETE(new ArrayList<>(List.of(PointStatus.COMPLETE))),
    ALL(new ArrayList<>(List.of(PointStatus.WITHDRAW, PointStatus.COMPLETE)));
    private final List<PointStatus> pointStatusList;
}
