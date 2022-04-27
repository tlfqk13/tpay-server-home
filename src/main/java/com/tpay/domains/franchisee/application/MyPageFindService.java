package com.tpay.domains.franchisee.application;

import com.tpay.domains.franchisee.application.dto.MyPageResponse;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantFindService;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.order.application.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageFindService {

    private final FranchiseeFindService franchiseeFindService;
    private final FranchiseeApplicantFindService franchiseeApplicantFindService;
    private final OrderService orderService;


    public MyPageResponse findByFranchiseeIndex(Long franchiseeIndex) {

        FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);
        Long totalSaleAmount = orderService.sumTotalSaleAmountByFranchiseeIndex(franchiseeIndex);
        FranchiseeApplicantEntity franchiseeApplicantEntity = franchiseeApplicantFindService.findByFranchiseeEntity(franchiseeEntity);
        return new MyPageResponse(franchiseeEntity, franchiseeApplicantEntity, totalSaleAmount);
    }
}
