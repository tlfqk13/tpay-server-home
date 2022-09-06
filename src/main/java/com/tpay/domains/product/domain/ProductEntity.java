package com.tpay.domains.product.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product")
@Entity
@ToString
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prdNm", length = 100)
    private String name;

    @Column(name = "lneNo", length = 3)
    private String lineNumber;

    @Column(name = "prdCode", length = 5)
    private String code;

    @Column(name = "indPrice", length = 10, unique = true)
    private String price;

    @Builder
    public ProductEntity(String name, String lineNumber, String code, String price) {
        this.name = name;
        this.lineNumber = lineNumber;
        this.code = code;
        this.price = price;
    }
}
