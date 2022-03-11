package com.tpay.domains.point.application;

import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.point.application.dto.PointWithdrawalRequest;
import com.tpay.domains.point.domain.PointEntity;
import com.tpay.domains.point.domain.PointRepository;
import com.tpay.domains.point.domain.PointStatus;
import com.tpay.domains.point.domain.SignType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PointWithdrawalService {

  private final PointRepository pointRepository;
  private final FranchiseeFindService franchiseeFindService;

  @Transactional
  public void pointWithdrawal(Long franchiseeIndex, PointWithdrawalRequest pointWithdrawalRequest) {
    Long amount = pointWithdrawalRequest.getAmount();
    FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);
    franchiseeEntity.withdrawalBalance(amount);
    PointEntity pointEntity = PointEntity.builder()
        .createdDate(LocalDateTime.now())
        .franchiseeEntity(franchiseeEntity)
        .pointStatus(PointStatus.WITHDRAW)
        .signType(SignType.NEGATIVE)
        .change(amount)
        .balance(franchiseeEntity.getBalance())
        .build();
    pointRepository.save(pointEntity);
    // TODO: 2022/03/11 return 해야하고, SCHEDULED DB만든 후 진행할 것

  }
}
