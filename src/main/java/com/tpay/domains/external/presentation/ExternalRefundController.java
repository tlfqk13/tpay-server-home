package com.tpay.domains.external.presentation;


import com.tpay.domains.external.application.dto.ExternalRefundApprovalRequest;
import com.tpay.domains.external.application.dto.ExternalRefundApprovalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExternalRefundController {

  @PostMapping("/external/refund")
  public ResponseEntity<ExternalRefundApprovalResponse> externalRefundApproval(
      // TODO: 2022/03/18 POS 관련 API 정해지면 작업 진행
      @RequestBody ExternalRefundApprovalRequest externalRefundApprovalRequest){
      return ResponseEntity.ok(ExternalRefundApprovalResponse.builder()
          .message(externalRefundApprovalRequest.getFranchiseeNumber()+"번 가맹점"+externalRefundApprovalRequest.getPassportNumber()+ "번 고객")
          .build());
  }
}
