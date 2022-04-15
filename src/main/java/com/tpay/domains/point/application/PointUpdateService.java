package com.tpay.domains.point.application;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.push.PushCategoryType;
import com.tpay.commons.push.PushType;
import com.tpay.domains.point.application.dto.PointUpdateRequest;
import com.tpay.domains.point.domain.PointEntity;
import com.tpay.domains.point.domain.PointRepository;
import com.tpay.domains.point.domain.PointStatus;
import com.tpay.domains.push.application.UserPushTokenService;
import com.tpay.domains.push.application.dto.NotificationDto;
import com.tpay.domains.push.domain.UserPushTokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PointUpdateService {

    private final PointRepository pointRepository;
    private final UserPushTokenService userPushTokenService;

    @Transactional
    public String updateStatus(Long pointsIndex, PointUpdateRequest pointUpdateRequest) {
        Boolean isRead = pointUpdateRequest.getIsRead();
        PointStatus pointStatus = pointUpdateRequest.getPointStatus();
        PointEntity pointEntity = pointRepository.findById(pointsIndex).orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid pointsIndex"));

        if (isRead == null) {
            PointStatus result = pointEntity.updateStatus(pointStatus);

            // 변경 요청한 포인트 상태가 COMPLETE일 경우 푸쉬
            if(pointUpdateRequest.getPointStatus().equals(PointStatus.COMPLETE)){
                UserPushTokenEntity userPushTokenEntity = userPushTokenService.findByFranchiseeIndex(pointEntity.getFranchiseeEntity().getId());

                Long change = pointEntity.getChange();
                int changeInt = Integer.parseInt(change.toString());
                new NotificationDto.Request(PushCategoryType.CASE_SEVEN, PushType.TOKEN,userPushTokenEntity.getUserToken(),changeInt);
            }
            return "pointStatus가 " + result + "로 변경되었습니다.";
        } else {
            Boolean result = pointEntity.updateIsRead(isRead);
            return "isRead가 " + result + "로 변경되었습니다.";
        }
    }
}
