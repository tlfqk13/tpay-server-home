package com.tpay.domains.franchisee_applicant.application;

import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.employee.application.EmployeeFindService;
import com.tpay.domains.employee.application.dto.EmployeeFindResponse;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantDetailResponse;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.franchisee_upload.application.FranchiseeBankFindService;
import com.tpay.domains.franchisee_upload.application.FranchiseeUploadFindService;
import com.tpay.domains.franchisee_upload.domain.FranchiseeBankEntity;
import com.tpay.domains.franchisee_upload.domain.FranchiseeUploadEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FranchiseeApplicantDetailService {

    private final FranchiseeApplicantFindService franchiseeApplicantFindService;
    private final FranchiseeUploadFindService franchiseeUploadFindService;
    private final FranchiseeBankFindService franchiseeBankFindService;
    private final EmployeeFindService employeeFindService;

    public FranchiseeApplicantDetailResponse detail(Long franchiseeApplicantIndex) {
        FranchiseeApplicantEntity franchiseeApplicantEntity = franchiseeApplicantFindService.findByIndex(franchiseeApplicantIndex);
        FranchiseeEntity franchiseeEntity = franchiseeApplicantEntity.getFranchiseeEntity();
        FranchiseeUploadEntity franchiseeUploadEntity = franchiseeUploadFindService.findByFranchiseeIndex(franchiseeEntity.getId());
        List<EmployeeFindResponse> employeeResponse = employeeFindService.findAllByFranchiseeId(franchiseeEntity.getId());
        FranchiseeBankEntity franchiseeBankEntity;
        try {
            franchiseeBankEntity = franchiseeBankFindService.findByFranchiseeEntity(franchiseeEntity);
        } catch (InvalidParameterException e) {
            franchiseeBankEntity = FranchiseeBankEntity.builder().build();
        }

        String refundAfterShop = "X";
        if(franchiseeEntity.getIsAfterRefund()){
            refundAfterShop = "O";
        }

        FranchiseeApplicantDetailResponse franchiseeApplicantDetailResponse = FranchiseeApplicantDetailResponse.builder()
            .storeName(franchiseeEntity.getStoreName())
            .sellerName(franchiseeEntity.getSellerName())
            .businessNumber(franchiseeEntity.getBusinessNumber())
            .storeTel(franchiseeEntity.getStoreTel())
            .email(franchiseeEntity.getEmail())
            .isTaxRefundShop(franchiseeEntity.getIsTaxRefundShop())
            .franchiseeStatus(franchiseeApplicantEntity.getFranchiseeStatus())
            .signboard(franchiseeEntity.getSignboard())
            .productCategory(franchiseeEntity.getProductCategory())
            .storeNumber(franchiseeEntity.getStoreNumber())
            .storeAddressBasic(franchiseeEntity.getStoreAddressBasic())
            .storeAddressDetail(franchiseeEntity.getStoreAddressDetail())
            .createdDate(franchiseeEntity.getCreatedDate())
            .isRead(franchiseeApplicantEntity.getIsRead())
            .refundAfterShop(refundAfterShop)

            .imageUrl((Optional.ofNullable(franchiseeUploadEntity.getS3Path()).orElse("")))
            .taxFreeStoreNumber((Optional.ofNullable(franchiseeUploadEntity.getTaxFreeStoreNumber()).orElse("")))
            .bankName((Optional.ofNullable(franchiseeBankEntity.getBankName()).orElse("")))
            .bankAccount((Optional.ofNullable(franchiseeBankEntity.getAccountNumber()).orElse("")))
            .withdrawalDate((Optional.ofNullable(franchiseeBankEntity.getWithdrawalDate()).orElse("")))
            .rejectReason((Optional.ofNullable(franchiseeApplicantEntity.getRejectReason()).orElse("")))
            .balancePercentage(franchiseeEntity.getBalancePercentage())

            .employeeFindResponseInterfaceList(employeeResponse)
            .build();
        return franchiseeApplicantDetailResponse;
    }
}
