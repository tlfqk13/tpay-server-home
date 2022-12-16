package com.tpay.domains.erp.deploy;

import com.tpay.domains.erp.deploy.dto.SearchAllResponse;
import com.tpay.domains.erp.deploy.service.AdminSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ErpSearchKeywordController {
    private final AdminSearchService adminSearchService;

    @GetMapping("/keywords")
    public ResponseEntity<SearchAllResponse> searchAllData(
    ){
        SearchAllResponse response = adminSearchService.searchAllData();
        return ResponseEntity.ok(response);
    }
}
