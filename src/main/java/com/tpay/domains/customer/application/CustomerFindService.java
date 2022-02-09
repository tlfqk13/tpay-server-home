package com.tpay.domains.customer.application;

import com.tpay.commons.aria.PassportNumberEncryptService;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerFindService {

  private final PassportNumberEncryptService passportNumberEncryptService;
  private final CustomerRepository customerRepository;
  private final CustomerSaveService customerSaveService;

  public CustomerEntity findByNationAndPassportNumber(String customerName, String passportNumber, String nation) {
    String encryptedPassportNumber = passportNumberEncryptService.encrypt(passportNumber);
    CustomerEntity customerEntity =
        customerRepository
            .findByNationAndPassportNumber(nation,encryptedPassportNumber)
            .orElseGet(() -> customerSaveService.saveByCustomerInfo(customerName, encryptedPassportNumber, nation));

    return customerEntity;
  }

  public CustomerEntity findByIndex(Long customerIndex) {
    CustomerEntity customerEntity =
        customerRepository
            .findById(customerIndex)
            .orElseThrow(
                () ->
                    new InvalidParameterException(
                        ExceptionState.INVALID_PARAMETER, "Invalid Customer Index"));

    return customerEntity;
  }
}
