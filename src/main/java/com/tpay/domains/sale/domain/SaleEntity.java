package com.tpay.domains.sale.domain;

import com.tpay.domains.customer.domain.CustomerEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sale")
@Entity
@ToString
public class SaleEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "saleDate", length = 30)
  private String saleDate;

  @Column(name = "totVat", length = 30)
  private String totalVat;

  @Column(name = "totAmt", length = 50)
  private String totalAmount;

  @Column(name = "totQty", length = 30)
  private String totalQuantity;

  @Column(name = "purchsSn", length = 50)
  private String orderNumber;

  @OneToOne
  @JoinColumn(name = "customer_id")
  private CustomerEntity customerEntity;

  @Builder
  public SaleEntity(
      String saleDate,
      String totalVat,
      String totalAmount,
      String totalQuantity,
      String orderNumber,
      CustomerEntity customerEntity) {
    this.saleDate = saleDate;
    this.totalVat = totalVat;
    this.totalAmount = totalAmount;
    this.totalQuantity = totalQuantity;
    this.orderNumber = orderNumber;
    this.customerEntity = customerEntity;
  }
}
