package com.tpay.domains.barcode.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "barcode")
@Entity
public class BarcodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "s3path")
    private String s3Path;

    @Builder
    public BarcodeEntity(String s3Path) {
       this.s3Path = s3Path;
    }


}
