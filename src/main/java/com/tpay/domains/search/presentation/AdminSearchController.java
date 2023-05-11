package com.tpay.domains.search.presentation;

import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantFindResponse;
import com.tpay.domains.refund.application.dto.RefundPagingFindResponse;
import com.tpay.domains.refund.domain.RefundStatus;
import com.tpay.domains.search.application.AdminSearchService;
import com.tpay.domains.search.application.dto.SearchAllResponse;
import com.tpay.domains.search.application.dto.SearchListInterface;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "어드민 - 검색 관련 ")
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminSearchController {

    private final AdminSearchService adminSearchService;

    @GetMapping("/keywords")
    public ResponseEntity<SearchAllResponse> searchAllData(
    ){
        SearchAllResponse response = adminSearchService.searchAllData();
        return ResponseEntity.ok(response);
    }
}
