package com.tpay.domains.point.application;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.push.PushCategoryType;
import com.tpay.domains.point.application.dto.PointUpdateRequest;
import com.tpay.domains.point.domain.PointEntity;
import com.tpay.domains.point.domain.PointRepository;
import com.tpay.domains.point.domain.PointStatus;
import com.tpay.domains.push.application.NonBatchPushService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PointUpdateService {

    private final PointRepository pointRepository;
    private final NonBatchPushService nonBatchPushService;

    @Transactional
    public String updateStatus(Long pointsIndex, PointUpdateRequest pointUpdateRequest) {
        Boolean isRead = pointUpdateRequest.getIsRead();
        PointStatus pointStatus = pointUpdateRequest.getPointStatus();
        PointEntity pointEntity = pointRepository.findById(pointsIndex).orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid pointsIndex"));

        if (isRead == null) {
            PointStatus result = pointEntity.updateStatus(pointStatus);

            // 변경 요청한 포인트 상태가 COMPLETE일 경우 푸쉬
            if (pointUpdateRequest.getPointStatus().equals(PointStatus.COMPLETE)) {
                nonBatchPushService.nonBatchPushNSave(PushCategoryType.CASE_SEVEN, pointEntity.getFranchiseeEntity().getId(), String.valueOf(pointEntity.getChange()));
            }
            return "pointStatus가 " + result + "로 변경되었습니다.";
        } else {
            Boolean result = pointEntity.updateIsRead(isRead);
            return "isRead가 " + result + "로 변경되었습니다.";
        }
    }
}
