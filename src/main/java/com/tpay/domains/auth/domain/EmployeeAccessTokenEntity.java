package com.tpay.domains.auth.domain;


import com.tpay.domains.employee.domain.EmployeeEntity;
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
}
