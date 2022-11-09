package com.tpay.domains.refund.application;

import com.tpay.commons.exception.detail.KtpApiException;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefundFindApiService {

    private final RefundRepository refundRepository;

    public RefundEntity findById(Long refundIndex) {

        return refundRepository.findById(refundIndex)
            .orElseThrow(() -> new KtpApiException("9001", "취소 시 입력된 refund id 에 해당하는 환급건을 찾을 수 없습니다"));
    }
}
