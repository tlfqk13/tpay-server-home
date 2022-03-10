package com.tpay.domains.point.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.point.application.dto.StatusUpdateResponseInterface;
import com.tpay.domains.point.domain.PointEntity;
import com.tpay.domains.point.domain.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PointsUpdateService {

  private final PointRepository pointRepository;

  public String updateStatus(Long franchiseeIndex, LocalDate scheduledDate) {
    Optional<List<StatusUpdateResponseInterface>> needUpdateEntity = pointRepository.findNeedUpdateEntity(franchiseeIndex, scheduledDate);
    if (needUpdateEntity.isEmpty()) {
      return "Nothing to Update Status";
    } else {
      List<Long> targetList = new ArrayList<>();
      needUpdateEntity.get().forEach(i -> targetList.add(i.getId()));
      targetList.stream().map(i -> pointRepository.findById(i)
                              .orElseThrow( () -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Point Id Not Exists"))
                              )
                        .forEach(PointEntity::updateStatus);
      return targetList.size()+"Entity was Updated";
    }
  }
}
