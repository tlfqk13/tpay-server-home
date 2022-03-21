package com.tpay.domains.external.presentation;


import com.tpay.domains.external.application.ExternalRefundApprovalService;
import com.tpay.domains.external.application.dto.ExternalRefundApprovalRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExternalRefundController {

  private final ExternalRefundApprovalService externalRefundApprovalService;

  @PostMapping("/external/refund")
  public ResponseEntity<RefundResponse> externalRefundApproval(
      // TODO: 2022/03/18 POS 관련 API 정해지면 작업 진행
      @RequestBody ExternalRefundApprovalRequest externalRefundApprovalRequest){

    RefundResponse approve = externalRefundApprovalService.approve(externalRefundApprovalRequest);
    return ResponseEntity.ok(approve);
  }
}
