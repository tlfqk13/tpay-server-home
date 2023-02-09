package com.tpay.domains.refund_upload.repository;

import com.tpay.domains.refund_upload.domain.RefundUploadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundReceiptUploadRepository extends JpaRepository<RefundUploadEntity,Long> {
}
