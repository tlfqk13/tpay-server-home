package com.tpay.domains.auth.application;

import com.tpay.commons.jwt.AuthToken;
import com.tpay.domains.auth.application.dto.EmployeeTokenInfo;
import com.tpay.domains.employee.application.EmployeeFindService;
import com.tpay.domains.employee.domain.EmployeeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeSignInService {

  private final EmployeeFindService employeeFindService;
  private final PasswordEncoder passwordEncoder;
  private final AuthService authService;



  @Transactional
  public EmployeeTokenInfo signIn(String userId, String password) {
    EmployeeEntity employeeEntity = employeeFindService.findByUserId(userId);
    if(!passwordEncoder.matches(password, employeeEntity.getPassword())){
      throw new IllegalArgumentException("Invalid Password");
    }

    AuthToken accessToken = authService.createAccessToken(employeeEntity);
    AuthToken refreshToken = authService.createRefreshToken(employeeEntity);
    authService.updateOrSave(employeeEntity, refreshToken.getValue());

    return EmployeeTokenInfo.builder()
        .employeeIndex(employeeEntity.getId())
        .userId(employeeEntity.getUserId())
        .name(employeeEntity.getName())
        .accessToken(accessToken.getValue())
        .refreshToken(refreshToken.getValue())
        .registeredDate(employeeEntity.getCreatedDate())
        .franchiseeIndex(employeeEntity.getFranchiseeEntity().getId())
        .build();



  }
}
