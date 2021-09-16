package com.tpay.domains.customer.application;

import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerFindService {

  private final CustomerRepository customerRepository;
  private final CustomerSaveService customerSaveService;

  public CustomerEntity findByCustomerNameAndPassportNumber(
      String customerName, String passportNumber, String nationality) {
    CustomerEntity customerEntity =
        customerRepository
            .findByCustomerNameAndPassportNumber(customerName, passportNumber)
            .orElseGet(
                () ->
                    customerSaveService.saveByCustomerInfo(
                        customerName, passportNumber, nationality));

    return customerEntity;
  }
}
