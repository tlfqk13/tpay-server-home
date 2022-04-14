package com.tpay.domains.batch.point_batch.application;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.util.DisappearDate;
import com.tpay.domains.batch.point_batch.application.dto.DeleteTargetList;
import com.tpay.domains.batch.point_batch.domain.PointDeletedRepository;
import com.tpay.domains.point.domain.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointDeleteService {

    private final PointRepository pointRepository;
    private final PointDeletedRepository pointDeletedRepository;

    @Transactional
    @Scheduled(cron = "0 1 * * * *")
    public String deletePoint() {
        LocalDate disappearDate = DisappearDate.DISAPPEAR_DATE.getDisappearDate();
        List<DeleteTargetList> targetIdList = pointRepository.findTargetIdList(disappearDate);
        List<Long> collect = targetIdList.stream().map(DeleteTargetList::getId).collect(Collectors.toList());
        collect.stream().map(id -> pointRepository.findById(id).orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid Point Id")))
            .forEach(pointDeletedRepository::save);
        pointRepository.deleteByIdList(collect);
        return targetIdList.size() + "건의 데이터가 삭제되었습니다.";
    }
}
