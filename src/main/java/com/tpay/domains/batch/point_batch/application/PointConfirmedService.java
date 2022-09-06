package com.tpay.domains.batch.point_batch.application;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.point.domain.PointEntity;
import com.tpay.domains.point.domain.PointRepository;
import com.tpay.domains.point.domain.PointStatus;
import com.tpay.domains.point.domain.SignType;
import com.tpay.domains.point_scheduled.domain.PointScheduledEntity;
import com.tpay.domains.point_scheduled.domain.PointScheduledRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointConfirmedService {

    private final PointRepository pointRepository;
    private final PointScheduledRepository pointScheduledRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void updateStatus() {
        LocalDateTime localDateTime = LocalDateTime.now().minusWeeks(2);
        List<PointScheduledEntity> pointScheduledEntityList = pointScheduledRepository.findByCreatedDateBeforeAndPointStatus(localDateTime, PointStatus.SCHEDULED);
        if (pointScheduledEntityList.isEmpty()) {
            log.trace("Nothing to Update - Point Status");
            return;
        }

        for (PointScheduledEntity pointScheduledEntity : pointScheduledEntityList) {

            OrderEntity orderEntity = pointScheduledEntity.getOrderEntity();
            FranchiseeEntity franchiseeEntity = orderEntity.getFranchiseeEntity();
            PointEntity pointEntity = PointEntity.builder()
                .createdDate(LocalDateTime.now())
                .signType(SignType.POSITIVE)
                .change(orderEntity.getPointsWithPercentage(franchiseeEntity.getBalancePercentage()))
                .pointStatus(PointStatus.SAVE)
                .balance(franchiseeEntity.getBalance())
                .franchiseeEntity(franchiseeEntity)
                .orderEntity(orderEntity)
                .withdrawalCheck(orderEntity.getPointsWithPercentage(franchiseeEntity.getBalancePercentage()))
                .build();
            pointRepository.save(pointEntity);
            pointScheduledEntity.updateStatusSave();
            franchiseeEntity.changeBalance(SignType.POSITIVE, orderEntity.getPointsWithPercentage(franchiseeEntity.getBalancePercentage()));
        }
        log.trace("{} Entity was Updated", pointScheduledEntityList.size());
    }

}
