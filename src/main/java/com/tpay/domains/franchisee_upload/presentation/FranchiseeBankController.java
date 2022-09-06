package com.tpay.domains.franchisee_upload.presentation;


import com.tpay.domains.franchisee_upload.application.FranchiseeBankService;
import com.tpay.domains.franchisee_upload.application.dto.FranchiseeBankInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 은행정보 업로드 관련
 */
@RestController
@RequestMapping("/franchisee/bank")
@RequiredArgsConstructor
public class FranchiseeBankController {

    private final FranchiseeBankService franchiseeBankService;

    /**
     * 은행 정보 조회
     */
    @GetMapping("/{franchiseeIndex}")
    public ResponseEntity<FranchiseeBankInfo> findMyAccount(@PathVariable Long franchiseeIndex) {
        FranchiseeBankInfo result = franchiseeBankService.findMyAccount(franchiseeIndex);
        return ResponseEntity.ok(result);
    }

    /**
     * 은행 정보 업데이트
     */
    @PatchMapping("/{franchiseeIndex}")
    public ResponseEntity<FranchiseeBankInfo> updateMyAccount(
        @PathVariable Long franchiseeIndex,
        @RequestBody FranchiseeBankInfo franchiseeBankInfo) {
        FranchiseeBankInfo result = franchiseeBankService.updateMyAccount(franchiseeIndex, franchiseeBankInfo);
        return ResponseEntity.ok(result);
    }
}
