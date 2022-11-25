package com.tpay.domains.erp.deploy.service;


import com.tpay.domains.erp.deploy.dto.SearchAllResponse;
import com.tpay.domains.erp.deploy.dto.SearchListInterface;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
