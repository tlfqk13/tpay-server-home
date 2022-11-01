package com.tpay.domains.customer.domain;

import com.tpay.domains.BaseTimeEntity;
import com.tpay.domains.customer.application.dto.CustomerDto;
import com.tpay.domains.customer.application.dto.CustomerPaymentType;
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

    @Column(name = "cusPhoneNumber", length = 40)
    private String customerPhoneNumber;
    @Column(name = "cusCreditNumber", length = 40)
    private String customerCreditNumber;
    @Column(name = "cusBankName", length = 40)
    private String customerBankName;

    @Column(name = "cusAccountNumber", length = 40)
    private String customerAccountNumber;

    @Column(name = "isRegister", length = 5)
    private Boolean isRegister;

    @Builder
    public CustomerEntity(String passportNumber, String customerName, String nation) {
        this.passportNumber = passportNumber;
        this.customerName = customerName;
        this.nation = nation;
    }

    public void registerAfterRefundCustomer(String passportNumber, String customerName, String nation,
                                            CustomerDto.Request customerInfo) {
        this.passportNumber = passportNumber;
        this.customerName = customerName;
        this.nation = nation;
        this.customerEmail = customerInfo.getEmail();
        this.customerPaymentType = customerInfo.getCustomerPaymentType();
        this.customerPhoneNumber = customerInfo.getPhoneNumber();
        this.customerCreditNumber = customerInfo.getCreditCardNumber();
        this.customerBankName = customerInfo.getBankName();
        this.customerAccountNumber = customerInfo.getAccountNumber();
    }

    public void updateRegister() {
        this.isRegister = true;
    }
}
