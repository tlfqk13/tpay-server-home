package com.tpay.domains.auth.application;


import com.tpay.domains.auth.domain.FranchiseeTokenRepository;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantRepository;
import com.tpay.domains.franchisee_upload.domain.FranchiseeBankRepository;
import com.tpay.domains.franchisee_upload.domain.FranchiseeUploadRepository;
import com.tpay.domains.order.domain.OrderRepository;
import com.tpay.domains.point.domain.PointRepository;
import com.tpay.domains.point_scheduled.domain.PointScheduledRepository;
import com.tpay.domains.push.domain.UserPushTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountDeleteService {

    private final FranchiseeRepository franchiseeRepository;
    private final FranchiseeTokenRepository franchiseeTokenRepository;
    private final FranchiseeFindService franchiseeFindService;
    private final UserPushTokenRepository userPushTokenRepository;
    private final AccessTokenService accessTokenService;
    private final FranchiseeApplicantRepository franchiseeApplicantRepository;
    private final FranchiseeUploadRepository franchiseeUploadRepository;

    private final PointScheduledRepository pointScheduledRepository;
    private final PointRepository pointRepository;
    private final FranchiseeBankRepository franchiseeBankRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public String deleteAccount(Long deleteIndex){

        //푸시토큰 삭제
        FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(deleteIndex);
        accessTokenService.deleteByFranchiseeEntityId(franchiseeEntity.getId());
        franchiseeApplicantRepository.deleteByFranchiseeEntity(franchiseeEntity);
        franchiseeTokenRepository.deleteByFranchiseeEntityId(franchiseeEntity.getId());
        userPushTokenRepository.deleteByFranchiseeEntity(franchiseeEntity);
        franchiseeUploadRepository.deleteByFranchiseeEntity(franchiseeEntity);
        pointScheduledRepository.deleteByFranchiseeEntityId(franchiseeEntity.getId());
        pointRepository.deleteByFranchiseeEntityId(franchiseeEntity.getId());
        franchiseeBankRepository.deleteByFranchiseeEntityId(franchiseeEntity.getId());
        orderRepository.deleteByFranchiseeEntityId(franchiseeEntity.getId());
        franchiseeRepository.deleteById(franchiseeEntity.getId());
        log.trace("==========================Account Delete===========================");
        log.trace("[사업자번호] : {}", franchiseeEntity.getBusinessNumber());
        log.trace("==========================Account Delete===========================");

        return "FRANCHISEE DELETE ACCOUNT";
    }
}
