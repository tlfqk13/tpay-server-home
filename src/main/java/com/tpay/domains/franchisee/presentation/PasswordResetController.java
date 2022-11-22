package com.tpay.domains.franchisee.presentation;

import com.tpay.commons.util.IndexInfo;
import com.tpay.commons.util.resolver.KtpIndexInfo;
import com.tpay.domains.franchisee.application.PasswordResetService;
import com.tpay.domains.franchisee.application.dto.PasswordChangeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 앱 내 패스워드 변경/재설정 관련
 */
@RestController
@RequestMapping("/franchisee/password")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    /**
     * 로그인 전 비밀번호 재설정 검증 단계
     */
    @GetMapping("/exists/{businessNumber}")
    public ResponseEntity<Boolean> existBusinessNumber(@PathVariable String businessNumber) {
        boolean result = passwordResetService.existBusinessNumber(businessNumber);
        return ResponseEntity.ok(result);
    }

    /**
     * 로그인 전 비밀번호 재설정 검증 단계
     */
    @GetMapping("/selfCertification/{businessNumber}")
    public ResponseEntity<Boolean> selfCertification(
        @PathVariable String businessNumber,
        @RequestParam String name,
        @RequestParam String phoneNumber
    ) {
        boolean result = passwordResetService.selfCertification(businessNumber, name, phoneNumber);
        return ResponseEntity.ok(result);
    }

    /**
     * 로그인 전 비밀번호 재설정
     */
    @PatchMapping("/out/{businessNumber}")
    public ResponseEntity<Boolean> resetOutPassword(
        @PathVariable String businessNumber,
        @RequestBody PasswordChangeRequest passwordChangeRequest
    ) {
        boolean result = passwordResetService.reset(businessNumber, passwordChangeRequest);
        return ResponseEntity.ok(result);
    }

    /**
     * 로그인 후 비밀번호 재설정 검증 단계
     */
    @GetMapping("/equals/{franchiseeIndex}")
    public ResponseEntity<Boolean> correctPassword(
            @PathVariable Long franchiseeIndex,
            @RequestParam String password,
            @KtpIndexInfo IndexInfo indexInfo
            ) {
        boolean result = passwordResetService.correctPassword(indexInfo.getIndex(), password);
        return ResponseEntity.ok(result);
    }

    /**
     * 로그인 후 비밀번호 재설정
     */
    @PatchMapping("/in/{franchiseeIndex}")
    public ResponseEntity<Boolean> resetPassword(
        @PathVariable Long franchiseeIndex,
        @RequestBody PasswordChangeRequest passwordChangeRequest,
        @KtpIndexInfo IndexInfo indexInfo) {
        boolean result = passwordResetService.change(indexInfo.getIndex(), passwordChangeRequest);
        return ResponseEntity.ok(result);
    }
}
