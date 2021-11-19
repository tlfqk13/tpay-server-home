package com.tpay.domains.customer.application;

import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerSaveService {

  private final CustomerRepository customerRepository;

  @Transactional
  public CustomerEntity saveByCustomerInfo(
      String customerName, String passportNumber, String nationality) {

    CustomerEntity customerEntity =
        customerRepository.save(
            CustomerEntity.builder()
                .customerName(customerName)
                .passportNumber(passportNumber)
                .nation(nationality)
                .build());

    return customerEntity;
  }
}
