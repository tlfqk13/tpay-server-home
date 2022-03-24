package com.tpay.domains.auth.domain;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.JwtRuntimeException;
import com.tpay.domains.BaseTimeEntity;
import com.tpay.domains.employee.domain.EmployeeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "employee_token")
@Entity
public class EmployeeTokenEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employeeEntity;

    private String refreshToken;

    @Builder
    public EmployeeTokenEntity(EmployeeEntity employeeEntity, String refreshToken) {
        this.employeeEntity = employeeEntity;
        this.refreshToken = refreshToken;
    }

    public EmployeeTokenEntity refreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public void validUser(Long parsedIndex) {
        if (!this.employeeEntity.getId().equals(parsedIndex)) {
            throw new JwtRuntimeException(ExceptionState.FORCE_REFRESH);
        }
    }

    public void validToken(String refreshToken) {
        if (!this.refreshToken.equals(refreshToken)) {
            throw new JwtRuntimeException(ExceptionState.FORCE_REFRESH);
        }
    }
}
