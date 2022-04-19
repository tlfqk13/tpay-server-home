package com.tpay.domains.employee.domain;


import com.tpay.domains.BaseTimeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "employee")
@Entity
public class EmployeeEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String userId;

    @NotNull
    private String password;

    @NotNull
    private Boolean isDelete;

    private boolean isActiveSound;
    private boolean isActiveVibration;

    @ManyToOne
    @JoinColumn(name = "franchisee_id", nullable = false)
    private FranchiseeEntity franchiseeEntity;

    public EmployeeEntity updateDelete() {
        this.isDelete = true;
        return this;
    }

    public EmployeeEntity updateInfo(String name, String password) {
        this.name = name;
        this.password = password;
        return this;
    }

    public EmployeeEntity updateInfo(String name) {
        this.name = name;
        return this;
    }

    public void updateSound(boolean isActiveSound){
        this.isActiveSound = isActiveSound;
    }
    public void updateVibration(boolean isActiveVibration){
        this.isActiveVibration = isActiveVibration;
    }
}
