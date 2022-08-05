package com.tpay.domains.auth.domain;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.JwtRuntimeException;
import com.tpay.domains.employee.domain.EmployeeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Table(name = "employee_accesstoken")
@Entity
public class EmployeeAccessTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employeeEntity;

    private String accessToken;

    @Builder
    public EmployeeAccessTokenEntity(EmployeeEntity employeeEntity, String accessToken) {
        this.employeeEntity = employeeEntity;
        this.accessToken = accessToken;
    }

    public EmployeeAccessTokenEntity accessToken(String accessToken){
        this.accessToken = accessToken;
        return this;
    }

    public void validUser(Long parsedIndex) {
        if (!this.employeeEntity.getId().equals(parsedIndex)) {
            throw new JwtRuntimeException(ExceptionState.FORCE_REFRESH);
        }
    }

    public void validToken(String accessToken) {
        if (!this.accessToken.equals(accessToken)) {
            System.out.println("@@@@@@@@@validToken@@@@@@@@@@");
        }
    }
/*
    public void validToken(String accessToken) {
        if (!this.accessToken.equals(accessToken)) {
            throw new JwtRuntimeException(ExceptionState.FORCE_REFRESH);
        }
    }*/
}
