package com.tpay.domains.employee.domain;


import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "employee")
@Entity
public class EmployeeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String employeeId;

  @NotNull
  private String password;

  @ManyToOne
  @JoinColumn(name = "franchisee_id", nullable = false)
  private FranchiseeEntity franchiseeEntity;

}
