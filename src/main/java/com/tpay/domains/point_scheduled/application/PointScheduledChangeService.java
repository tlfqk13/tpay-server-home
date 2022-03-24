package com.tpay.domains.point_scheduled.application;


import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.point.domain.PointStatus;
import com.tpay.domains.point.domain.SignType;
import com.tpay.domains.point_scheduled.domain.PointScheduledEntity;
import com.tpay.domains.point_scheduled.domain.PointScheduledRepository;
import com.tpay.domains.refund.domain.RefundEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointScheduledChangeService {

    private final PointScheduledRepository pointScheduledRepository;

    public PointScheduledEntity change(RefundEntity refundEntity, SignType signType) {
        OrderEntity orderEntity = refundEntity.getOrderEntity();
        FranchiseeEntity franchiseeEntity = orderEntity.getFranchiseeEntity();
        long points = orderEntity.getPoints();
        PointStatus pointStatus;
        if (signType.equals(SignType.POSITIVE)) {
            pointStatus = PointStatus.SCHEDULED;
        } else {
            pointStatus = PointStatus.CANCEL;
        }

        PointScheduledEntity entity = PointScheduledEntity.builder()
            .franchiseeEntity(franchiseeEntity)
            .orderEntity(orderEntity)
            .pointStatus(pointStatus)
            .value(points)
            .build();

        return pointScheduledRepository.save(entity);
    }
}
