package com.tpay.domains.customer.application;

import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerSaveService {

    private final CustomerRepository customerRepository;

    @Transactional
    public CustomerEntity saveByCustomerInfo(String customerName, String passportNumber, String nation) {

        CustomerEntity customerEntity =
            customerRepository.save(
                CustomerEntity.builder()
                    .customerName(customerName)
                    .passportNumber(passportNumber)
                    .nation(nation)
                    .build());

        return customerEntity;
    }
}
