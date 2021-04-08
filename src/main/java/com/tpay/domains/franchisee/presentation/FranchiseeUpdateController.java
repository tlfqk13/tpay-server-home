package com.tpay.domains.franchisee.presentation;

import com.tpay.domains.franchisee.application.FranchiseeUpdateService;
import com.tpay.domains.franchisee.application.dto.FranchiseeUpdateRequest;
import com.tpay.domains.franchisee.application.dto.FranchiseeUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FranchiseeUpdateController {

  private final FranchiseeUpdateService franchiseeUpdateService;

  @PatchMapping("/franchisee")
  public ResponseEntity<FranchiseeUpdateResponse> update(
      @RequestBody FranchiseeUpdateRequest franchiseeUpdateRequest) {
    return franchiseeUpdateService.update(franchiseeUpdateRequest);
  }
}
