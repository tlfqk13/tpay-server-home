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
import java.util.stream.Collectors;

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

            // Scheduled 테이블 상태변경
            List<Long> targetList = new ArrayList<>();
            needUpdateEntity.get().forEach(i -> targetList.add(i.getId()));
            for (Long aLong : targetList) {
                System.out.println(aLong);
            }
            targetList.stream().map(i -> pointScheduledRepository.findById(i)
                    .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Point Id Not Exists"))
                )
                .forEach(PointScheduledEntity::updateStatus);

            // 프랜차이즈 balance 변경
            // TODO: 2022/03/15 기능 추가에 의해 상태변경과 balance 로직이 따로 구현되어있으나, 추후 리팩토링 요망
            List<Long> saveTargetList = new ArrayList<>();
            needUpdateEntity.get().forEach(i -> saveTargetList.add(i.getOrderId()));
            List<OrderEntity> orderEntityList = saveTargetList.stream().map(orderFindService::findById)
                .collect(Collectors.toList());

            for (OrderEntity orderEntity : orderEntityList) {
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
