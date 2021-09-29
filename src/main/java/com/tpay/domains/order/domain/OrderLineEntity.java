package com.tpay.domains.order.domain;

import com.tpay.domains.product.domain.ProductEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orderline")
@Entity
public class OrderLineEntity {

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
  @JoinColumn(name = "order_id")
  private OrderEntity orderEntity;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private ProductEntity productEntity;

  @Builder
  public OrderLineEntity(String quantity, OrderEntity orderEntity, ProductEntity productEntity) {
    this.quantity = quantity;
    this.orderEntity = orderEntity;
    this.productEntity = productEntity;
    initialize();
  }

  public void initialize() {
    this.totalPrice = initTotalPrice();
    this.vat = initVAT();
  }

  private String initTotalPrice() {
    return String.valueOf(
        Long.parseLong(this.quantity) * Long.parseLong(this.productEntity.getPrice()));
  }

  private String initVAT() {
    return String.valueOf(Math.floorDiv(Long.parseLong(this.totalPrice), 11));
  }
}
