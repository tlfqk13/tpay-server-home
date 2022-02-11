package com.tpay.domains.customer.application;

import com.tpay.commons.aria.PassportNumberEncryptService;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.exception.detail.InvalidPassportInfoException;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerFindService {

  private final PassportNumberEncryptService passportNumberEncryptService;
  private final CustomerRepository customerRepository;
  private final CustomerSaveService customerSaveService;

  public CustomerEntity findByNationAndPassportNumber(String customerName, String passportNumber, String nation) {
    String encryptedPassportNumber = passportNumberEncryptService.encrypt(passportNumber);
    Optional<CustomerEntity> optionalCustomerEntity =
        customerRepository
            .findByNationAndPassportNumber(nation,encryptedPassportNumber);
    if (optionalCustomerEntity.isEmpty()) {
      if(existByPassportNumber(passportNumber)) {
        throw new InvalidPassportInfoException(ExceptionState.INVALID_PASSPORT_INFO,"passportNumber Already Exists(might incorrect nation)");
      }
      else return customerSaveService.saveByCustomerInfo(customerName,encryptedPassportNumber,nation);
    }
    else return optionalCustomerEntity.get();
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

  public boolean existByPassportNumber(String passportNumber) {
    String encryptPassportNumber = passportNumberEncryptService.encrypt(passportNumber);
    return customerRepository.existsByPassportNumber(encryptPassportNumber);
  }
}
