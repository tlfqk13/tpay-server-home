package com.tpay.domains.refund.application;

import com.tpay.commons.aws.S3FileUploader;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.exception.detail.UnknownException;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.refund.application.dto.RefundReceiptDto;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.refund_upload.domain.RefundUploadEntity;
import com.tpay.domains.refund_upload.repository.RefundReceiptUploadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefundReceiptUploadService {

    private final S3FileUploader s3FileUploader;
    private final RefundRepository refundRepository;
    private final RefundReceiptUploadRepository refundReceiptUploadRepository;

    @Transactional
    public String uploadReceiptImage(Long refundIndex, MultipartFile image) {
        try {

            RefundEntity refundEntity = refundRepository.findById(refundIndex)
                    .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid RefundIndex"));

            String s3Path = s3FileUploader.uploadJpg(refundEntity.getId(), image);

            RefundUploadEntity refundUploadEntity = refundReceiptUploadRepository.save(RefundUploadEntity
                    .builder()
                    .refundS3Path(s3Path)
                    .build());

            refundEntity.addReceiptUpload(refundUploadEntity);

            CustomerEntity customerEntity = refundEntity.getOrderEntity().getCustomerEntity();

            List<RefundReceiptDto.Response> response = getRefundReceipt(customerEntity);

            updateIsReceiptUpload(customerEntity, response);

            return s3Path;

        } catch (Exception e) {
            throw new UnknownException(ExceptionState.UNKNOWN, " 영수증 저장 실패");
        }
    }

    private List<RefundReceiptDto.Response> getRefundReceipt(CustomerEntity customerEntity) {
        List<RefundReceiptDto.Response> response = refundRepository.findRefundReceipt(customerEntity.getPassportNumber(), true);
        if (!"KOR".equals(customerEntity.getNation())) {
            response = response.stream().filter(r -> Integer.parseInt(r.getTotalRefund()) >= 75000).collect(Collectors.toList());
        }
        return response;
    }

    private void updateIsReceiptUpload(CustomerEntity customerEntity, List<RefundReceiptDto.Response> response) {

        int count = (int) response.stream()
                .filter(r -> r.getRefundS3Path() != null).count();

        log.trace(" @@ count = {}", count);
        log.trace(" @@ response.size() = {}", response.size());

        boolean isReceiptUpload = false;

        if (response.size() == count) {
            customerEntity.updateIsReceiptUpload(!isReceiptUpload);
        } else {
            customerEntity.updateIsReceiptUpload(isReceiptUpload);
        }
    }

    @Transactional
    public void deleteReceiptImage(Long refundIndex) {
        RefundEntity refundEntity = refundRepository.findById(refundIndex)
                .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid RefundIndex"));

        CustomerEntity customerEntity = refundEntity.getOrderEntity().getCustomerEntity();

        List<RefundReceiptDto.Response> response = getRefundReceipt(customerEntity);

        refundReceiptUploadRepository.deleteById(refundEntity.getReceiptUpload().getId());
        log.trace(" @@ refundEntity.getReceiptUpload().getId() = {}", refundEntity.getReceiptUpload().getId());

        updateIsReceiptUpload(customerEntity, response);
    }

    @Transactional
    public String updateReceiptImage(Long refundIndex, MultipartFile uploadImage) {

        try {
            RefundEntity refundEntity = refundRepository.findById(refundIndex)
                    .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid RefundIndex"));

            String s3Path = s3FileUploader.uploadJpg(refundEntity.getId(), uploadImage);

            refundEntity.getReceiptUpload().updateRefundS3Path(s3Path);

            return s3Path;

        } catch (Exception e) {
            throw new UnknownException(ExceptionState.UNKNOWN, " 영수증 저장 실패");
        }

    }
}
