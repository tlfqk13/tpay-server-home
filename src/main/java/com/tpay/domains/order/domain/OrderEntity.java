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
@Table(name = "orders")
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
  public OrderEntity(CustomerEntity customerEntity, FranchiseeEntity franchiseeEntity) {
    this.customerEntity = customerEntity;
    this.franchiseeEntity = franchiseeEntity;
    this.orderLineEntityList = new LinkedList<>();
    this.initialize();
  }

  public void initialize() {
    String saleDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    this.saleDate = saleDate;
    this.totalAmount = initOrderLineAmount();
    this.totalQuantity = initOrderLineQuantity();
    this.totalVat = initOrderLineVAT();
  }

  public String initOrderLineAmount() {
    Long amount =
        this.orderLineEntityList.stream()
            .map(orderLine -> Long.parseLong(orderLine.getTotalPrice()))
            .reduce(Long::sum)
            .orElseGet(() -> 0L);
    return String.valueOf(amount);
  }

  public String initOrderLineQuantity() {
    Long quantity =
        this.orderLineEntityList.stream()
            .map(orderLine -> Long.parseLong(orderLine.getQuantity()))
            .reduce(Long::sum)
            .orElseGet(() -> 0L);
    return String.valueOf(quantity);
  }

  public String initOrderLineVAT() {
    Long VAT =
        this.orderLineEntityList.stream()
            .map(orderLine -> Long.parseLong(orderLine.getVat()))
            .reduce(Long::sum)
            .orElseGet(() -> 0L);
    return String.valueOf(VAT);
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

  public String getTotalRefund() {
    double amount = Double.parseDouble(this.totalAmount);
    int totalRefund = (int) Math.floor(amount * 70) / 100;
    return Integer.toString(totalRefund);
  }

  public long getPoints() {
    double amount = Double.parseDouble(this.totalAmount);
    long points = (long) Math.floor(amount * 30) / 100;
    return points;
  }

  public OrderEntity setOrderNumber(String orderNumber) {
    this.orderNumber = orderNumber;
    return this;
  }
}
