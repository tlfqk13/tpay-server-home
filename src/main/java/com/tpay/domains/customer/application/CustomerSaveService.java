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
  public CustomerEntity saveByCustomerInfo(String customerName, String passportNumber, String nation) {

    // TODO: 2022/02/09 아래 검증은 이미 암호화된 여권번호이므로 다른곳으로 옮겨야함 추후 개선할 것
//    if(!regExUtils.validate(RegExType.PASSPORT,passportNumber)){
//     throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER,"Invalid passportNumber format");
//    }

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
