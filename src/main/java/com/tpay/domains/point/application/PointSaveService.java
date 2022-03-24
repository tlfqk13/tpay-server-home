package com.tpay.domains.point.application;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.point.domain.PointEntity;
import com.tpay.domains.point.domain.PointRepository;
import com.tpay.domains.point.domain.PointStatus;
import com.tpay.domains.point.domain.SignType;
import com.tpay.domains.refund.domain.RefundEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PointSaveService {

    private final PointRepository pointRepository;

    @Transactional
    public PointEntity save(RefundEntity refundEntity) {
        OrderEntity orderEntity = refundEntity.getOrderEntity();
        FranchiseeEntity franchiseeEntity = orderEntity.getFranchiseeEntity();

        long points = orderEntity.getPoints();
//    franchiseeEntity.changeBalance(SignType.POSITIVE, points);

        PointEntity pointEntity =
            PointEntity.builder()
                .createdDate(LocalDateTime.now())
                .signType(SignType.POSITIVE)
                .change(points)
                .pointStatus(PointStatus.SCHEDULED)
                .balance(franchiseeEntity.getBalance())
                .franchiseeEntity(franchiseeEntity)
                .orderEntity(orderEntity)
                .build();

        return pointRepository.save(pointEntity);
    }
}
