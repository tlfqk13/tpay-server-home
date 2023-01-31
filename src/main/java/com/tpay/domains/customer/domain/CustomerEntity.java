package com.tpay.domains.customer.domain;

import com.tpay.domains.BaseTimeEntity;
import com.tpay.domains.customer.application.dto.CustomerDto;
import com.tpay.domains.customer.application.dto.CustomerPaymentType;
import com.tpay.domains.customer.application.dto.DepartureStatus;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "customer")
@Entity
@ToString
public class CustomerEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "cusPassNo", length = 63, nullable = false)
    private String passportNumber;

    @NotNull
    @Column(name = "cusNm", length = 40, nullable = false)
    private String customerName;

    @NotNull
    @Column(name = "cusNatn", length = 3, nullable = false)
    private String nation;

    @Column(name = "cusEmail", length = 40)
    private String customerEmail;

    @Column(name = "paymentType")
    private CustomerPaymentType customerPaymentType;

    @Column(name = "cusCreditNumber", length = 40)
    private String customerCreditNumber;
    @Column(name = "cusBankName", length = 40)
    private String customerBankName;

    @Column(name = "cusAccountNumber", length = 40)
    private String customerAccountNumber;

    @Column(name = "isRegister", length = 5)
    private Boolean isRegister;
    @Enumerated(EnumType.STRING)
    private DepartureStatus departureStatus;

    private String departureDate;
    @Builder
    public CustomerEntity(String passportNumber, String customerName, String nation) {
        this.passportNumber = passportNumber;
        this.customerName = customerName;
        this.nation = nation;
        this.isRegister = false;
    }

    public void registerAfterRefundCustomer(String passportNumber,String nation, CustomerDto.Request customerInfo) {
        this.passportNumber = passportNumber;
        this.customerName = customerInfo.getName();
        this.nation = nation;
        this.customerEmail = customerInfo.getEmail();
        this.customerPaymentType = customerInfo.getCustomerPaymentType();
        this.customerCreditNumber = customerInfo.getCreditCardNumber();
        this.customerBankName = customerInfo.getBankName();
        this.customerAccountNumber = customerInfo.getAccountNumber();
    }

    public void updateDepartureStatus(){
        this.departureStatus = DepartureStatus.DEPARTURE_WAIT;
    }

    public void updateRegister() {
        this.isRegister = true;
    }

    public void updateCustomerPaymentInfo(CustomerPaymentType customerPaymentType, String customerCreditNumber, String customerBankName, String customerAccountNumber){
        this.customerPaymentType =customerPaymentType;
        this.customerCreditNumber = customerCreditNumber;
        this.customerBankName = customerBankName;
        this.customerAccountNumber = customerAccountNumber;
    }

    public void updateDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }
}
