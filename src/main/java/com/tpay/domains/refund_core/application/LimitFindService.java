package com.tpay.domains.refund_core.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidPassportInfoException;
import com.tpay.commons.webClient.WebRequestUtil;
import com.tpay.domains.customer.application.CustomerService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import com.tpay.domains.refund_core.application.dto.CheckNationValue;
import com.tpay.domains.refund_core.application.dto.RefundLimitRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;


@RequiredArgsConstructor
@Service
@Slf4j
public class LimitFindService {

    private final CustomerService customerService;
    private final WebRequestUtil webRequestUtil;

    private final FranchiseeRepository franchiseeRepository;

    @Transactional
    public RefundResponse find(RefundLimitRequest request) {
        String uri = CustomValue.REFUND_SERVER + "/refund/limit";

        // TODO: 2022/10/11 독일 여권일 경우, D -> DEU
        if(checkNation(request)){
            nationUpdate(request);
        }

        // 한도 조회 요청
        RefundResponse refundResponse = webRequestUtil.post(uri, request);
        log.debug("request.passportNumber = {} , refundResponse.getPassportNumber() = {}"
                , request.getPassportNumber()
                , refundResponse.getPassportNumber());

        log.debug("request.nation = {} , refundResponse.getNation() = {}"
                , request.getNationality()
                , refundResponse.getNationality());

        // 한도 조회 요청 후, 성공되면 고객 정보 등록
        if(refundResponse.getResponseCode().equals("0000")) {
            Long customerEntityId;
            Optional<CustomerEntity> customerEntityOptional = customerService.findCustomerByNationAndPassportNumber(refundResponse.getPassportNumber(), refundResponse.getNationality());
            if (customerEntityOptional.isEmpty()) {
                log.debug("Customer Not exists.");
                CustomerEntity customerEntity = customerService.updateCustomerInfo(request.getName(), refundResponse.getPassportNumber(), refundResponse.getNationality());
                customerEntityId = customerEntity.getId();
                log.debug("Refund Limit customerID = {}", customerEntityId);
            } else {
                log.debug("Customer already exists.");
                customerEntityId = customerEntityOptional.get().getId();
                log.debug("Refund Limit customerID = {}", customerEntityId);
            }

            // TODO: 2022/11/04 사후환급 신청 가맹점 여부 조회를 위해... 
            FranchiseeEntity franchiseeEntity = franchiseeRepository.findById(request.getFranchiseeIndex()).orElseThrow(() -> new IllegalArgumentException("Invalid Franchisee Entity"));

            return refundResponse.addCustomerInfo(customerEntityId,franchiseeEntity.getIsAfterRefund());

        } else {
            throw new InvalidPassportInfoException(ExceptionState.INVALID_PASSPORT_INFO, "한도조회 실패");
        }
    }

    private boolean checkNation(RefundLimitRequest request) {
        if(CheckNationValue.NATION_GERMANY_D.equals(request.getNationality())){
            log.debug(" @@ CheckNationValue = {}", request.getNationality());
            return true;
        }else{
            return false;
        }
    }

    private void nationUpdate(RefundLimitRequest request){
        request.nationUpdate(CheckNationValue.NATION_GERMANY_DEU);
    }
}
