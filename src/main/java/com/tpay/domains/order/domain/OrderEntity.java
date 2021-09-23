package com.tpay.domains.order.domain;

import com.tpay.domains.BaseTimeEntity;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.refund_core.application.dto.RefundProductInfo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sale")
@Entity
public class OrderEntity extends BaseTimeEntity {
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

  @ManyToOne
  @JoinColumn(name = "customer_id", nullable = false)
  private CustomerEntity customerEntity;

  @ManyToOne
  @JoinColumn(name = "franchisee_id", nullable = false)
  private FranchiseeEntity franchiseeEntity;

  @OneToMany(mappedBy = "orderEntity", cascade = CascadeType.PERSIST)
  private List<OrderLineEntity> orderLineEntityList;

  @Builder
  public OrderEntity(
      String totalVat,
      String totalAmount,
      String totalQuantity,
      CustomerEntity customerEntity,
      FranchiseeEntity franchiseeEntity) {
    this.totalVat = totalVat;
    this.totalAmount = totalAmount;
    this.totalQuantity = totalQuantity;
    this.customerEntity = customerEntity;
    this.franchiseeEntity = franchiseeEntity;
    this.orderLineEntityList = new LinkedList<>();
    totalLineQuantity();
    totalLineAmount();
    totalLineVat();
    setSaleDate();
  }

  public void totalLineQuantity() {
    Long sumQuantity =
        this.orderLineEntityList.stream()
            .map(sale -> Long.parseLong(sale.getQuantity()))
            .reduce(Long::sum)
            .get();
    this.totalQuantity = String.valueOf(sumQuantity);
  }

  public void totalLineAmount() {
    Long sumAmount =
        this.orderLineEntityList.stream()
            .map(sale -> Long.parseLong(sale.getTotalPrice()))
            .reduce(Long::sum)
            .get();
    this.totalAmount = String.valueOf(sumAmount);
  }

  public void totalLineVat() {
    Long sumVat =
        this.orderLineEntityList.stream()
            .map(sale -> Long.parseLong(sale.getVat()))
            .reduce(Long::sum)
            .get();
    this.totalVat = String.valueOf(sumVat);
  }

  public void setSaleDate() {
    String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    this.saleDate = now;
  }

  public OrderEntity addOrderLine(OrderLineEntity orderLineEntity) {
    this.orderLineEntityList.add(orderLineEntity);
    return this;
  }

  public List<RefundProductInfo> getRefundProductInfoList() {
    return this.orderLineEntityList.stream()
        .map(orderLineEntity -> RefundProductInfo.of(orderLineEntity))
        .collect(Collectors.toList());
  }
}
