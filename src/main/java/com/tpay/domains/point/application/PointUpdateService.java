package com.tpay.domains.point.application;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.point.application.dto.PointUpdateRequest;
import com.tpay.domains.point.domain.PointEntity;
import com.tpay.domains.point.domain.PointRepository;
import com.tpay.domains.point.domain.PointStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PointUpdateService {

    private final PointRepository pointRepository;

    @Transactional
    public String updateStatus(Long pointsIndex, PointUpdateRequest pointUpdateRequest) {
        Boolean isRead = pointUpdateRequest.getIsRead();
        PointStatus pointStatus = pointUpdateRequest.getPointStatus();
        PointEntity pointEntity = pointRepository.findById(pointsIndex).orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid pointsIndex"));
        if (isRead == null) {
            PointStatus result = pointEntity.updateStatus(pointStatus);
            return "pointStatus가 " + result + "로 변경되었습니다.";
        } else {
            Boolean result = pointEntity.updateIsRead(isRead);
            return "isRead가 " + result + "로 변경되었습니다.";
        }
    }
}
