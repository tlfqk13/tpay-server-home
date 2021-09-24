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
public class PointCancelService {

  private final PointRepository pointRepository;

  @Transactional
  public void cancel(RefundEntity refundEntity) {
    OrderEntity orderEntity = refundEntity.getOrderEntity();
    FranchiseeEntity franchiseeEntity = orderEntity.getFranchiseeEntity();

    long points = orderEntity.getPoints();
    franchiseeEntity.changeBalance(SignType.NEGATIVE, points);
    PointEntity pointEntity =
        PointEntity.builder()
            .createdDate(LocalDateTime.now())
            .signType(SignType.NEGATIVE)
            .change(points)
            .pointStatus(PointStatus.CANCEL)
            .balance(franchiseeEntity.getBalance())
            .franchiseeEntity(franchiseeEntity)
            .build();

    pointRepository.save(pointEntity);
  }
}
