package com.tpay.domains.point.application;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.point.domain.PointEntity;
import com.tpay.domains.point.domain.PointRepository;
import com.tpay.domains.point.domain.PointStatus;
import com.tpay.domains.point.domain.SignType;
import com.tpay.domains.refund.domain.RefundEntity;
import java.time.LocalDateTime;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointSaveService {

  private final PointRepository pointRepository;

  @Transactional
  public PointEntity save(RefundEntity refundEntity) {
    OrderEntity orderEntity = refundEntity.getOrderEntity();
    FranchiseeEntity franchiseeEntity = orderEntity.getFranchiseeEntity();

    long points = orderEntity.getPoints();
    franchiseeEntity.changeBalance(SignType.POSITIVE, points);

    PointEntity pointEntity =
        PointEntity.builder()
            .createdDate(LocalDateTime.now())
            .signType(SignType.POSITIVE)
            .change(points)
            .pointStatus(PointStatus.SAVE)
            .balance(franchiseeEntity.getBalance())
            .franchiseeEntity(franchiseeEntity)
            .build();

    return pointRepository.save(pointEntity);
  }
}
