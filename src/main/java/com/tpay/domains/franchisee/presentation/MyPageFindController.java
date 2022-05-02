package com.tpay.domains.franchisee.presentation;


import com.tpay.domains.franchisee.application.MyPageFindService;
import com.tpay.domains.franchisee.application.dto.MyPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MyPageFindController {

    private final MyPageFindService myPageFindService;

    @GetMapping("/franchisee/{franchiseeIndex}")
    public ResponseEntity<MyPageResponse> findMyPageInfo(
        @PathVariable Long franchiseeIndex) {
        MyPageResponse response = myPageFindService.findByFranchiseeIndex(franchiseeIndex);
        return ResponseEntity.ok(response);
    }
}
