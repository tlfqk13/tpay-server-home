package com.tpay.domains.point_batch.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.order.application.OrderFindService;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.point.application.dto.StatusUpdateResponseInterface;
import com.tpay.domains.point.domain.PointEntity;
import com.tpay.domains.point.domain.PointRepository;
import com.tpay.domains.point.domain.PointStatus;
import com.tpay.domains.point.domain.SignType;
import com.tpay.domains.point_scheduled.domain.PointScheduledEntity;
import com.tpay.domains.point_scheduled.domain.PointScheduledRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PointConfirmedService {

    private final PointRepository pointRepository;
    private final PointScheduledRepository pointScheduledRepository;
    private final OrderFindService orderFindService;

    @Transactional
    public String updateStatus() {
        LocalDate scheduledDate = LocalDate.now().minusWeeks(2);
        Optional<List<StatusUpdateResponseInterface>> needUpdateEntity = pointScheduledRepository.findNeedUpdateEntity(scheduledDate);
        if (needUpdateEntity.get().isEmpty()) {
            System.out.println("Nothing to Update Status");
            return "Nothing to Update Status";
        } else {

            List<Long> targetList = new ArrayList<>();
            needUpdateEntity.get().forEach(i -> targetList.add(i.getId()));
            for (Long aLong : targetList) {
                System.out.println(aLong);
                // Scheduled 테이블 상태변경
                PointScheduledEntity pointScheduledEntity = pointScheduledRepository.findById(aLong).orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Point Id Not Exists"));
                pointScheduledEntity.updateStatus();

                // 포인트 테이블 Save
                OrderEntity orderEntity = orderFindService.findById(aLong);
                FranchiseeEntity franchiseeEntity = orderEntity.getFranchiseeEntity();
                franchiseeEntity.changeBalance(SignType.POSITIVE, orderEntity.getPoints());
                PointEntity pointEntity = PointEntity.builder()
                    .createdDate(LocalDateTime.now())
                    .signType(SignType.POSITIVE)
                    .change(orderEntity.getPoints())
                    .pointStatus(PointStatus.SAVE)
                    .balance(franchiseeEntity.getBalance())
                    .orderEntity(orderEntity)
                    .build();
                pointRepository.save(pointEntity);

            }
            System.out.println(targetList.size() + "Entity was Updated");
            return targetList.size() + "Entity was Updated";
        }
    }
}
