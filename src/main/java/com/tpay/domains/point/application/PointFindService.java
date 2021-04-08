package com.tpay.domains.point.application;

import com.tpay.domains.point.application.dto.PointRequest;
import com.tpay.domains.point.application.dto.PointResponse;
import com.tpay.domains.point.domain.PointEntity;
import com.tpay.domains.point.domain.PointRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointFindService {

  private final PointRepository pointRepository;

  public ResponseEntity<List<PointResponse>> findPoints(
      Integer page, Integer size, PointRequest pointRequest) {
    LocalDate endDate = LocalDate.now();
    LocalDate startDate =
        pointRequest.getWeek() > 0
            ? endDate.minusWeeks(pointRequest.getWeek())
            : endDate.minusMonths(pointRequest.getMonth());

    PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdDate").descending());
    List<PointEntity> pointEntityList =
        pointRepository.findAllByFranchiseeEntityIdAndCreatedDateBetween(
            pointRequest.getFranchiseeId(),
            startDate.atStartOfDay(),
            endDate.atStartOfDay(),
            pageRequest);

    List<PointResponse> pointResponseList =
        pointEntityList.stream()
            .map(
                pointEntity -> {
                  String createdDateAsString =
                      pointEntity
                          .getCreatedDate()
                          .format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH.mm.ss"));
                  return PointResponse.builder()
                      .datetime(createdDateAsString)
                      .pointStatus(pointEntity.getPointStatus())
                      .value(pointEntity.getChange())
                      .build();
                })
            .collect(Collectors.toList());

    return ResponseEntity.ok(pointResponseList);
  }
}
