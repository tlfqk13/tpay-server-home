package com.tpay.domains.customer.domain;

import com.tpay.domains.customer.application.dto.CustomerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerRepositoryCustom {

    Page<CustomerDto.Response> findAllCustomer(Pageable pageable, String searchKeyword);
}
