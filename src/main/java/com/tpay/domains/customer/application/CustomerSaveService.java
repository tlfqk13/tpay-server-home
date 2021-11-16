package com.tpay.domains.customer.application;

import com.tpay.commons.aria.PassportNumberEncryptService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerSaveService {

  private final PassportNumberEncryptService passportNumberEncryptService;
  private final CustomerRepository customerRepository;

  public CustomerEntity saveByCustomerInfo(
      String customerName, String passportNumber, String nationality) {

    String encryptedPassportNumber = passportNumberEncryptService.encrypt(passportNumber);

    CustomerEntity customerEntity =
        CustomerEntity.builder()
            .customerName(customerName)
            .passportNumber(encryptedPassportNumber)
            .nation(nationality)
            .build();

    return customerRepository.save(customerEntity);
  }
}
