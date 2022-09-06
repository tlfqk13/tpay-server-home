package com.tpay.domains.franchisee.presentation;

import com.tpay.domains.franchisee.application.SignUpService;
import com.tpay.domains.franchisee.application.dto.FranchiseeSignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody FranchiseeSignUpRequest request) {
        signUpService.signUp(request);
        return ResponseEntity.ok().build();
    }
}
