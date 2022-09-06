package com.tpay.domains.employee.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.employee.application.dto.EmployeeDeleteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeDeleteService {

    private final EmployeeFindService employeeFindService;

    @Transactional
    public void delete(EmployeeDeleteRequest employeeDeleteRequest) {

        List<Long> deleteIndexList = employeeDeleteRequest.getEmployeeIndexList();

        deleteIndexList.forEach(
            deleteIndex ->
                employeeFindService.findById(deleteIndex).orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "EmployeeIndex doesn't exists")).updateDelete());
    }
}
