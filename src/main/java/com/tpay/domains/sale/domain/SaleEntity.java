package com.tpay.domains.sale.domain;

import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
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

  @Column(name = "saleDatm", length = 14, nullable = false)
  private String saleDate;

  @Column(name = "totVat", length = 12)
  private String totalVat;

  @Column(name = "totAmt", length = 12)
  private String totalAmount;

  @Column(name = "totQty", length = 7)
  private String totalQuantity;

  @Column(name = "purchsSn", length = 20, nullable = false)
  private String orderNumber;

  @OneToOne
  @JoinColumn(name = "customer_id", nullable = false)
  private CustomerEntity customerEntity;

  @ManyToOne
  @JoinColumn(name = "franchisee_id", nullable = false)
  private FranchiseeEntity franchiseeEntity;

  @Builder
  public SaleEntity(
      String saleDate,
      String totalVat,
      String totalAmount,
      String totalQuantity,
      String orderNumber,
      CustomerEntity customerEntity,
      FranchiseeEntity franchiseeEntity) {
    this.saleDate = saleDate;
    this.totalVat = totalVat;
    this.totalAmount = totalAmount;
    this.totalQuantity = totalQuantity;
    this.orderNumber = orderNumber;
    this.customerEntity = customerEntity;
    this.franchiseeEntity = franchiseeEntity;
  }
}
