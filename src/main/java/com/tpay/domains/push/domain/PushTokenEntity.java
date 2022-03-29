package com.tpay.domains.push.domain;


import com.tpay.domains.employee.domain.EmployeeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "push_token")
@Entity
public class PushTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String pushToken;

    @OneToOne
    @JoinColumn(name = "franchisee_id")
    private FranchiseeEntity franchiseeEntity;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employeeEntity;

    @Builder
    public PushTokenEntity(String pushToken, FranchiseeEntity franchiseeEntity, EmployeeEntity employeeEntity) {
        this.pushToken = pushToken;
        this.franchiseeEntity = franchiseeEntity;
        this.employeeEntity = employeeEntity;
    }
}
