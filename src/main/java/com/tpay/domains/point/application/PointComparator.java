package com.tpay.domains.point.application;

import com.tpay.domains.point.application.dto.PointInfo;

import java.util.Comparator;

public class PointComparator implements Comparator<PointInfo> {
    @Override
    public int compare(PointInfo o1, PointInfo o2) {
        Long o2Long = Long.parseLong(o2.getDatetime().replaceAll("\\.", "").replaceAll(" ", ""));
        Long o1Long = Long.parseLong(o1.getDatetime().replaceAll("\\.", "").replaceAll(" ", ""));
        Long l = o2Long - o1Long;
        return l.intValue();
    }
}
