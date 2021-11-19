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

  public CustomerEntity findByCustomerNameAndPassportNumber(
      String customerName, String passportNumber, String nationality) {
    String encryptedPassportNumber = passportNumberEncryptService.encrypt(passportNumber);
    CustomerEntity customerEntity =
        customerRepository
            .findByCustomerNameAndPassportNumber(customerName, encryptedPassportNumber)
            .orElseGet(
                () ->
                    customerSaveService.saveByCustomerInfo(
                        customerName, encryptedPassportNumber, nationality));

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
