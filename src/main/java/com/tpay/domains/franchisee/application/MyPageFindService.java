package com.tpay.domains.franchisee.application;

import com.tpay.domains.franchisee.application.dto.MyPageResponse;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantFindService;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.order.application.OrderFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageFindService {

    private final FranchiseeFindService franchiseeFindService;
    private final FranchiseeApplicantFindService franchiseeApplicantFindService;
    private final OrderFindService orderFindService;


    public MyPageResponse findByFranchiseeIndex(Long franchiseeIndex) {

        FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);
        Long totalSaleAmount = orderFindService.sumTotalSaleAmountByFranchiseeIndex(franchiseeIndex);
        FranchiseeApplicantEntity franchiseeApplicantEntity = franchiseeApplicantFindService.findByFranchiseeEntity(franchiseeEntity);
        return new MyPageResponse(franchiseeEntity,franchiseeApplicantEntity,totalSaleAmount);
    }
}
