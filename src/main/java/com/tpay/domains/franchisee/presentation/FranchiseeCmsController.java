package com.tpay.domains.franchisee.presentation;


import com.tpay.domains.franchisee.application.FranchiseeCmsService;
import com.tpay.domains.franchisee.application.dto.cms.FranchiseeCmsResponseDetailInterface;
import com.tpay.domains.franchisee.application.dto.cms.FranchiseeCmsResponseInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FranchiseeCmsController {

  private final FranchiseeCmsService franchiseeCmsService;

  @GetMapping("/franchisee/{franchiseeIndex}/cms")
  public ResponseEntity<FranchiseeCmsResponseInterface> cmsReport(
      @PathVariable Long franchiseeIndex,
      @RequestParam String requestDate
  ) {
    FranchiseeCmsResponseInterface result = franchiseeCmsService.cmsReport(franchiseeIndex, requestDate);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/franchisee/{franchiseeIndex}/cms/detail")
  public ResponseEntity<FranchiseeCmsResponseDetailInterface> cmsDetail(
      @PathVariable Long franchiseeIndex,
      @RequestParam String requestDate
  ) {
    FranchiseeCmsResponseDetailInterface result = franchiseeCmsService.cmsDetail(franchiseeIndex, requestDate);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/franchisee/{franchiseeIndex}/cms/downloads")
  public ResponseEntity<String> cmsDownloads (
      @PathVariable Long franchiseeIndex,
      @RequestParam String requestDate
  ) {
    String result = franchiseeCmsService.cmsDownloads(franchiseeIndex, requestDate);
    return ResponseEntity.ok("Asdf");
  }
}
