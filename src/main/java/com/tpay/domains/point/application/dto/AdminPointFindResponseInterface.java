package com.tpay.domains.point.application.dto;


import com.tpay.domains.point.domain.PointStatus;

public interface AdminPointFindResponseInterface {
    Long getPointsIndex();

    PointStatus getPointStatus();

    String getBusinessNumber();

    String getStoreName();

    String getSellerName();

    String getRequestedDate();

    String getAmount();

    Boolean getIsRead();
}
