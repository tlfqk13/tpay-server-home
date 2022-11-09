package com.tpay.domains.customer.application;

import com.tpay.commons.aria.PassportNumberEncryptService;
import com.tpay.commons.exception.detail.KtpApiException;
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
public class CustomerApiService {

    private final PassportNumberEncryptService passportNumberEncryptService;
    private final CustomerRepository customerRepository;

    public Optional<CustomerEntity> findCustomerByNationAndPassportNumber(String passportNumber, String nation) {
        String encryptedPassportNumber = encryptPassportNum(passportNumber);
        return customerRepository.findByNationAndPassportNumber(nation, encryptedPassportNumber);
    }

    @Transactional
    public CustomerEntity updateCustomerInfo(String customerName, String passportNumber, String nation) {
        String encryptPassportNumber = encryptPassportNum(passportNumber);
        if (customerRepository.existsByPassportNumber(encryptPassportNumber)) {
            throw new KtpApiException("4001", "이미 저장된 여권번호에 대한 국적이 일치하지 않습니다");
        }

        return customerRepository.save(
                CustomerEntity.builder()
                        .customerName(customerName)
                        .passportNumber(encryptPassportNumber)
                        .nation(nation)
                        .build());
    }

    private String encryptPassportNum(String passportNumber) {
        return passportNumberEncryptService.encrypt(passportNumber);
    }

    public CustomerEntity findByIndex(Long customerIndex) {
        return customerRepository
                .findById(customerIndex)
                .orElseThrow(
                        () -> new KtpApiException("9001", "취소 시 입력된 customer id 에 해당하는 고객 정보를 찾을 수 없습니다")
                );
    }
}
