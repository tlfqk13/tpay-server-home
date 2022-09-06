package com.tpay.domains.auth.presentation;

import com.tpay.domains.auth.application.security.SignInSecurityService;
import com.tpay.domains.auth.application.dto.SignInDto;
import com.tpay.domains.auth.application.dto.SignInTokenInfo;
import com.tpay.domains.auth.application.security.SignOutSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Profile(value = "security")
public class AuthSecurityController {

    private final SignInSecurityService signInService;
    private final SignOutSecurityService signOutService;
    /**
     * Spring security Sign-in and out
     * Not applied yet
     */

    @PostMapping("/sign-in-new")
    ResponseEntity<SignInTokenInfo> signIn(@RequestBody SignInDto.Request signInRequest) {
        SignInTokenInfo signInTokenInfo = signInService.signIn(signInRequest);
        return ResponseEntity.ok(signInTokenInfo);
    }

    @DeleteMapping("/sign-out-new")
    public ResponseEntity<String> signOut() {
        String result = signOutService.signOutNew();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/test-access")
    ResponseEntity<String> test() {
        return ResponseEntity.ok("ok");
    }
}
