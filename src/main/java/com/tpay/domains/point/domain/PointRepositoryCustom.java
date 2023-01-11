package com.tpay.domains.point.domain;

import java.time.LocalDateTime;

public interface PointRepositoryCustom {

    Long findDisappearPoints(Long franchiseeId, LocalDateTime disappearDate);

    Long findScheduledPoints(Long franchiseeId);

    Long findTotalPoints(Long franchiseeId);
}
