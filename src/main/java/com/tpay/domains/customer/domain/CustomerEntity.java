package com.tpay.domains.customer.domain;

import com.tpay.domains.BaseTimeEntity;
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

    @Builder
    public CustomerEntity(String passportNumber, String customerName, String nation) {
        this.passportNumber = passportNumber;
        this.customerName = customerName;
        this.nation = nation;
    }
}
