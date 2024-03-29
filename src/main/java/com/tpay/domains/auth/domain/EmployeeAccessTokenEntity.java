package com.tpay.domains.auth.domain;


import com.tpay.domains.BaseTimeEntity;
import com.tpay.domains.employee.domain.EmployeeEntity;
import com.tpay.domains.refund.application.dto.Device;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Table(name = "employee_accesstoken")
@Entity
public class EmployeeAccessTokenEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employeeEntity;

    private String accessToken;

    private String name;
    private String os;
    private String appVersion;

    @Builder
    public EmployeeAccessTokenEntity(EmployeeEntity employeeEntity, String accessToken) {
        this.employeeEntity = employeeEntity;
        this.accessToken = accessToken;
    }
    @Builder
    public EmployeeAccessTokenEntity(EmployeeEntity employeeEntity, String accessToken, Device device) {
        this.employeeEntity = employeeEntity;
        this.accessToken = accessToken;
        this.name = device.getName();
        this.os = device.getOs();
        this.appVersion = device.getAppVersion();
    }

    public void updateDeviceInfo(String name, String os, String appVersion){
        this.name = name;
        this.os = os;
        this.appVersion = appVersion;
    }

    public EmployeeAccessTokenEntity accessToken(String accessToken){
        this.accessToken = accessToken;
        return this;
    }
}
