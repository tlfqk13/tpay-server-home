package com.tpay.domains.refund_upload.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refund_upload")
@Getter
@AllArgsConstructor
@Builder
public class RefundUploadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String refundS3Path;

    public void updateRefundS3Path( String refundS3Path){
        this.refundS3Path = refundS3Path;
    }

}
