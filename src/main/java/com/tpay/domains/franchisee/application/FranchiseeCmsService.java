package com.tpay.domains.franchisee.application;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.franchisee.application.dto.cms.FranchiseeCmsResponseInterface;
import com.tpay.domains.order.domain.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FranchiseeCmsService {

  private final OrderRepository orderRepository;

  public FranchiseeCmsResponseInterface cmsReport(Long franchiseeIndex, String requestDate) {
    String year = requestDate.substring(0,4);
    String month = requestDate.substring(4);
    int monthInt = Integer.parseInt(month);
    if( !(requestDate.length() == 6 && monthInt <= 12 && monthInt >= 1)) {
      throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER,"Invalid Date format");
    }
    return orderRepository.findMonthlyCmsReport(franchiseeIndex, year, month);
  }
}
