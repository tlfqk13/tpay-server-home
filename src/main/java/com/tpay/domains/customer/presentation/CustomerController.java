package com.tpay.domains.customer.presentation;

import com.tpay.domains.customer.application.CustomerService;
import com.tpay.domains.customer.application.dto.CustomerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * 사후 환급 개인정보입력
 */
@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /**
     * 여권 유효성 검사
     */
    @PostMapping("/passport-validate")
    public ResponseEntity<Boolean> customerPassportValidate(
            @RequestBody CustomerDto.Request customerInfo){

        Boolean response = customerService.customerPassportValidate(customerInfo);

        return ResponseEntity.ok(response);
    }

    /**
     * 사후 환급 개인 정보 입력
     */
    @PostMapping("/refund-info")
    public ResponseEntity<String> registerAfterRefundCustomer(
            @RequestBody CustomerDto.Request customerInfo){

        customerService.registerAfterRefundCustomer(customerInfo);

        return ResponseEntity.ok("ok");
    }

    /**
     * 사후 환급 개인 정보 수정하기전에 입력했던 정보 보여주기 용도
     */
    @GetMapping ("/refund-info")
    public ResponseEntity<CustomerDto.Response> getRegisterAfterRefundCustomer(
            @RequestBody CustomerDto.Request customerInfo){

        CustomerDto.Response response = customerService.getRegisterAfterRefundCustomer(customerInfo);

        return ResponseEntity.ok(response);
    }

    /**
     * 사후 환급 개인 정보 수정
     */
    @PatchMapping ("/refund-info")
    public ResponseEntity<String> updateRegisterAfterRefundCustomer(
            @RequestBody CustomerDto.Request customerInfo){

        customerService.registerAfterRefundCustomer(customerInfo);

        return ResponseEntity.ok("ok");
    }

    /**
     * 사후 환급 개인 정보 수정
     */
    @PatchMapping ("/admin/refund-info")
    public ResponseEntity<CustomerDto.Response> findAll(
            @RequestBody CustomerDto.Request customerInfo){

        CustomerDto.Response response =customerService.findAll(customerInfo);

        return ResponseEntity.ok(response);
    }
}
