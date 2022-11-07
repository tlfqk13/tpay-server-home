package com.tpay.domains.api.service;

import com.tpay.domains.api.domain.vo.RefundApprovalDto;
import com.tpay.domains.customer.application.CustomerService;
import com.tpay.domains.customer.domain.CustomerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApiService {

    private final CustomerService customerService;

    @Transactional
    public Long createCustomer(RefundApprovalDto.Request dto) {
        Optional<CustomerEntity> customerOptional = customerService.findCustomerByNationAndPassportNumber(dto.getPassport(), dto.getNation());
        if(customerOptional.isPresent()){
            return customerOptional.get().getId();
        } else {
            CustomerEntity customerEntity = customerService.updateCustomerInfo(dto.getName(), dto.getPassport(), dto.getNation());
            return customerEntity.getId();
        }
    }
}
