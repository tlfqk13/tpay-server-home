package com.tpay.domains.batch.point_batch.application;


import com.tpay.commons.util.DisappearDate;
import com.tpay.domains.batch.point_batch.domain.PointDeletedEntity;
import com.tpay.domains.batch.point_batch.domain.PointDeletedRepository;
import com.tpay.domains.point.domain.PointEntity;
import com.tpay.domains.point.domain.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PointDeleteService {

    private final PointRepository pointRepository;
    private final PointDeletedRepository pointDeletedRepository;

    @Transactional
    @Scheduled(cron = "0 0 16 * * *")
    public String deletePoint() {
        LocalDateTime disappearDate = DisappearDate.DISAPPEAR_DATE.getDisappearDate();
        List<PointEntity> pointEntityList = pointRepository.findByCreatedDateBefore(disappearDate);
        if (pointEntityList.isEmpty()) {
            System.out.println("Nothing to Update - Point Deleted");
            return "Nothing to Update - Point Deleted";
        } else {
            pointEntityList.stream().map(PointDeletedEntity::new).forEach(pointDeletedRepository::save);
            pointRepository.deleteInBatch(pointEntityList);
            return pointEntityList.size() + "건의 데이터가 삭제되었습니다.";
        }
    }
}
