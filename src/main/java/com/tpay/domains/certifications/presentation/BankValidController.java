//package com.tpay.domains.certifications.presentation;
//
//
//import com.tpay.domains.certifications.application.BankValidService;
//import com.tpay.domains.certifications.application.dto.BankValidRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//public class BankValidController {
//
//  private final BankValidService bankValidService;
//
//  @PostMapping("/validate/bank")
//  public ResponseEntity validate(@RequestBody BankValidRequest bankValidRequest){
//    bankValidService.validate(bankValidRequest);
//    return ResponseEntity.ok().build();
//  }
//}
