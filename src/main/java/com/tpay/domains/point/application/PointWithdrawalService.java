package com.tpay.domains.point.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_upload.application.FranchiseeBankFindService;
import com.tpay.domains.franchisee_upload.domain.FranchiseeBankEntity;
import com.tpay.domains.point.application.dto.PointWithdrawalRequest;
import com.tpay.domains.point.application.dto.PointWithdrawalResponse;
import com.tpay.domains.point.application.dto.WithdrawalFindNextInterface;
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
  private final FranchiseeBankFindService franchiseeBankFindService;

  @Transactional
  public PointWithdrawalResponse pointWithdrawal(Long franchiseeIndex, PointWithdrawalRequest pointWithdrawalRequest) {
    Long amount = pointWithdrawalRequest.getAmount();
    FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);
    FranchiseeBankEntity franchiseeBankEntity = franchiseeBankFindService.findByFranchiseeEntity(franchiseeEntity);

    long withdrawalCheckAmount = amount;

    while (withdrawalCheckAmount != 0L) {
      // TODO: 2022/03/15 findById는 비효율적임. Entity로 리턴하는 NativeQuery를 적용시키는 게 좋을 것 같음
      WithdrawalFindNextInterface next = pointRepository.findNext(franchiseeIndex);
      PointEntity pointEntity = pointRepository.findById(next.getId()).orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid PointId [pintWithdrawal]"));
      Long nextWithdrawalCheck = next.getWithdrawalCheck();
      if(nextWithdrawalCheck <= withdrawalCheckAmount){
        withdrawalCheckAmount -= nextWithdrawalCheck;
        pointEntity.updateWithdrawalCheck(nextWithdrawalCheck);
      }
      else {
       pointEntity.updateWithdrawalCheck(withdrawalCheckAmount);
       withdrawalCheckAmount = 0L;
      }
    }

    //가맹점 Balance 변경
    franchiseeEntity.withdrawalBalance(amount);

    //Point Entity 생성
    PointEntity pointEntity = PointEntity.builder()
        .createdDate(LocalDateTime.now())
        .franchiseeEntity(franchiseeEntity)
        .pointStatus(PointStatus.WITHDRAW)
        .signType(SignType.NEGATIVE)
        .change(amount)
        .balance(franchiseeEntity.getBalance())
        .build();
    pointRepository.save(pointEntity);

    return PointWithdrawalResponse.builder()
        .amount(amount)
        .bankName(franchiseeBankEntity.getBankName())
        .accountNumber(franchiseeBankEntity.getAccountNumber())
        .restPoint(franchiseeEntity.getBalance())
        .build();


  }
}
