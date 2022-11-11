package com.tpay.domains.customer.application;

import com.tpay.commons.aria.PassportNumberEncryptService;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.exception.detail.InvalidPassportInfoException;
import com.tpay.domains.customer.application.dto.CustomerDto;
import com.tpay.domains.customer.application.dto.CustomerMyPageDto;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import com.tpay.domains.refund.application.dto.RefundReceiptDto;
import com.tpay.domains.refund.domain.RefundRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final PassportNumberEncryptService passportNumberEncryptService;
    private final CustomerRepository customerRepository;
    private final RefundRepository refundRepository;

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

    @Transactional
    public void registerAfterRefundCustomer(CustomerDto.Request customerInfo){

        CustomerEntity customerEntity = getCustomerEntity(customerInfo);

        customerEntity.registerAfterRefundCustomer(customerEntity.getPassportNumber(),customerEntity.getCustomerName()
                ,customerEntity.getNation(),customerInfo);

        customerEntity.updateRegister();

        customerRepository.save(customerEntity);
    }

    public boolean customerPassportValidate(CustomerDto.Request customerInfo) {

        CustomerEntity customerEntity = getCustomerEntity(customerInfo);
        return customerEntity.getIsRegister();
    }
    public CustomerDto.Response getRegisterAfterRefundCustomer(CustomerDto.Request customerInfo) {

        CustomerEntity customerEntity = getCustomerEntity(customerInfo);

        return CustomerDto.Response.builder()
                .passportNumber(customerInfo.getPassportNumber())
                .name(customerEntity.getCustomerName())
                .email(customerEntity.getCustomerEmail())
                .customerPaymentType(customerEntity.getCustomerPaymentType())
                .creditCardNumber(customerEntity.getCustomerCreditNumber())
                .bankName(customerEntity.getCustomerBankName())
                .accountNumber(customerEntity.getCustomerAccountNumber())
                .nation(customerEntity.getNation())
                .build();
    }

    public Page<CustomerDto.Response> findAllCustomer(int page, String searchKeyword) {

        PageRequest pageRequest = PageRequest.of(page, 15);
        boolean searchKeywordEmpty = searchKeyword.isEmpty();

        Page<CustomerDto.Response> responses =
                customerRepository.findAllCustomer(pageRequest,searchKeyword,searchKeywordEmpty);

        return responses;
    }

    public CustomerMyPageDto.Response getMyPage(CustomerDto.Request customerInfo) {

        CustomerEntity customerEntity = getCustomerEntity(customerInfo);

        List<RefundReceiptDto.Response> response =
                refundRepository.findRefundReceipt(customerEntity.getPassportNumber(),false);

        int totalRefundCompleted = 0;
        String refundInformation = CustomerCustomValue.CREDIT_CARD;

        if(!response.isEmpty()){
            totalRefundCompleted = response.size();
        }

        if(customerEntity.getCustomerCreditNumber().isEmpty()
                && !customerEntity.getCustomerBankName().isEmpty()
                && !customerEntity.getCustomerAccountNumber().isEmpty()){

            refundInformation = CustomerCustomValue.CARD;
        }

        return CustomerMyPageDto.Response.builder()
                .totalRefundCompleted(totalRefundCompleted)
                .refundInformation(refundInformation)
                .build();
    }

    private CustomerEntity getCustomerEntity(CustomerDto.Request customerInfo) {
        String encryptPassportNumber = passportNumberEncryptService.encrypt(customerInfo.getPassportNumber());

        return customerRepository.findByPassportNumber(encryptPassportNumber)
                .orElseThrow(()->new InvalidPassportInfoException(ExceptionState.INVALID_PASSPORT_INFO, "여권 조회 실패"));
    }
}
