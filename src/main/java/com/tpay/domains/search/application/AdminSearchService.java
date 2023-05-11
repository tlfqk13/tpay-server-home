package com.tpay.domains.search.application;


import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantFindResponse;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantInfo;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantRepository;
import com.tpay.domains.refund.application.dto.RefundFindResponseInterface;
import com.tpay.domains.refund.application.dto.RefundPagingFindResponse;
import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.refund.domain.RefundStatus;
import com.tpay.domains.search.application.dto.SearchAllResponse;
import com.tpay.domains.search.application.dto.SearchListInterface;
import com.tpay.domains.search.application.dto.SearchRefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminSearchService {

    private final FranchiseeApplicantRepository franchiseeApplicantRepository;

    public SearchAllResponse searchAllData() {
        List<SearchListInterface> searchList = null;
        List<String> businessNumberList = new ArrayList<>();
        List<String> storeNameList = new ArrayList<>();
        searchList = franchiseeApplicantRepository.findSearchListFranchisee();
        for(SearchListInterface searchListInterface : searchList ){
            businessNumberList.add(searchListInterface.getBusinessNumber());
            storeNameList.add(searchListInterface.getStoreName());
        }
        return SearchAllResponse.builder()
                .searchAllBusinessNumberList(businessNumberList)
                .searchAllStoreNameList(storeNameList)
                .build();
    }
}
