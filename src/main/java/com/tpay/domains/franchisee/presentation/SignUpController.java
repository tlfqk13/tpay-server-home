package com.tpay.domains.franchisee.presentation;

import com.tpay.commons.custom.CustomValue;
import com.tpay.domains.franchisee.application.SignUpService;
import com.tpay.domains.franchisee.application.dto.FranchiseeSignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping("/sign-up")
    public ResponseEntity<Long> signUp(HttpServletRequest http, @RequestBody FranchiseeSignUpRequest request) {
        Long franchiseeId = signUpService.signUp(request, isApiServer(http.getRemoteAddr()));
        return ResponseEntity.ok(franchiseeId);
    }

    private boolean isApiServer(String ip) {
        // TODO : API 서버 주소 입력
        List<String> allowedIps = List.of("127.0.0.1",    // Localhost
                CustomValue.API_TEST_SERVER);// Ktp-api-test

        return allowedIps.contains(ip);
    }
}
