package com.tpay.domains.customer.application;

import com.tpay.commons.aria.PassportNumberEncryptService;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.exception.detail.InvalidPassportInfoException;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerUpdateService {

    private final PassportNumberEncryptService passportNumberEncryptService;
    private final CustomerRepository customerRepository;

    public Optional<CustomerEntity> findCustomerByNationAndPassportNumber(String passportNumber, String nation) {
        String encryptedPassportNumber = passportNumberEncryptService.encrypt(passportNumber);
        return customerRepository.findByNationAndPassportNumber(nation, encryptedPassportNumber);
    }

    @Transactional
    public CustomerEntity updateCustomerInfo(String customerName, String passportNumber, String nation) {
        String encryptPassportNumber = passportNumberEncryptService.encrypt(passportNumber);
        if (customerRepository.existsByPassportNumber(encryptPassportNumber)) {
            throw new InvalidPassportInfoException(ExceptionState.INVALID_PASSPORT_INFO, "passportNumber Already Exists(might incorrect nation)");
        }
        log.trace("New customer register process");
        return customerRepository.save(
                CustomerEntity.builder()
                        .customerName(customerName)
                        .passportNumber(encryptPassportNumber)
                        .nation(nation)
                        .build());
    }

    public CustomerEntity findByIndex(Long customerIndex) {
        return customerRepository
                .findById(customerIndex)
                .orElseThrow(
                        () -> new InvalidParameterException(
                                ExceptionState.INVALID_PARAMETER, "Invalid Customer Index")
                );

    }
}
