package com.tpay.domains.sale.domain;

import com.tpay.domains.product.domain.ProductEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "saleline")
@Entity
@ToString
public class SaleLineEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "indQty", length = 5)
  private String quantity;

  @Column(name = "salePrice", length = 10)
  private String totalPrice;

  @Column(name = "indVat", length = 10)
  private String vat;

  @ManyToOne
  @JoinColumn(name = "sale_id")
  private SaleEntity saleEntity;

  @OneToOne
  @JoinColumn(name = "product_id")
  private ProductEntity productEntity;

  @Builder
  public SaleLineEntity(String quantity, SaleEntity saleEntity, ProductEntity productEntity) {
    this.quantity = quantity;
    this.saleEntity = saleEntity;
    this.productEntity = productEntity;
    calculateTotalPrice();
    calculateVAT();
  }

  private void calculateTotalPrice() {
    this.totalPrice =
        String.valueOf(
            Long.parseLong(this.quantity) * Long.parseLong(this.productEntity.getPrice()));
  }

  private void calculateVAT() {
    this.vat = String.valueOf(Double.parseDouble(this.totalPrice) / 11);
  }
}
