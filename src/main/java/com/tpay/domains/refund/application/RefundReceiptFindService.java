package com.tpay.domains.refund.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.refund.application.dto.RefundReceiptDto;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RefundReceiptFindService {

    private final RefundRepository refundRepository;

    public RefundEntity findById(Long refundIndex) {

        return refundRepository.findById(refundIndex)
            .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid RefundIndex"));
    }

    public List<RefundReceiptDto> findRefundReceiptDetail(String passportNumber){
        String taxFreeStoreNumber;//사후면세판매자 지정번호
        String sellerName;//판매자
        String franchiseeName;//상호
        String businessNumber;//사업자번호
        String storeAddress;//주소
        String storeTelNumber;// 연락처

        String saleDate;//판매일
        //단가= 금액 - 부가가치세
        String totalAmount;//금액
        //판매총액 = 금액
        String totalVat;//부가가치세 (VAT)
        String totalRefund;//즉시환급상당액
        //결제금액 = 금액
        String passportNumber1;//여권번호

        // order - franchiseeId - franchiseeUpload (지정번호)
        //       - franchisee - 판매자, 상호, 사업자번호, 주소, 연락처,

        // order - refundId - 판매일, 금액, 부가가치세, 즉시환급상당액
        List<RefundReceiptDto.Response> response = refundRepository.findRefundReceipt(passportNumber);
        // customer - order - 여권번호


        return null;
    }
}
