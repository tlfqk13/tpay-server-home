package com.tpay.domains.employee.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.util.SettingSelector;
import com.tpay.domains.employee.application.dto.EmployeeSettingDto;
import com.tpay.domains.employee.domain.EmployeeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeSettingService {

    private final EmployeeFindService employeeFindService;

    public EmployeeSettingDto.Response changeSoundOrVibration(Long employeeIndex, EmployeeSettingDto.Request request) {

        EmployeeEntity employeeEntity = employeeFindService.findById(employeeIndex).orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid Employee Index"));

        String s;
        if (request.getSettingSelector().equals(SettingSelector.SOUND)) {
            employeeEntity.updateSound(request.getIsActiveSound());
            s = "SOUND : " + request.getIsActiveSound();
        } else if (request.getSettingSelector().equals(SettingSelector.VIBRATION)) {
            employeeEntity.updateVibration(request.getIsActiveVibration());
            s = "VIBRATION : " + request.getIsActiveVibration();
        } else {
            s = "Nothing to Update";
        }
        return EmployeeSettingDto.Response.builder()
            .message(s)
            .build();
    }
}
