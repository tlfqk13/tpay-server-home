package com.tpay.domains.employee.application.dto;


import lombok.Getter;

@Getter
public class EmployeeRegistrationRequest {
  private String name;
  private String userId;
  private String password;
  private String passwordCheck;
}
