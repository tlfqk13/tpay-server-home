package com.tpay.domains.customer.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.regex.RegExType;
import com.tpay.commons.regex.RegExUtils;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerSaveService {

  private final CustomerRepository customerRepository;
  private final RegExUtils regExUtils;

  @Transactional
  public CustomerEntity saveByCustomerInfo(String customerName, String passportNumber, String nationality) {

    if(!regExUtils.validate(RegExType.PASSPORT,passportNumber)){
     throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER,"Invalid passportNumber format");
    }

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
