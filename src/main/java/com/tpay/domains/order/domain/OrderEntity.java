package com.tpay.domains.order.domain;

import com.tpay.domains.BaseTimeEntity;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.employee.domain.EmployeeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund_core.application.dto.RefundProductInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Column(name = "purchsSn", length = 20)
    private String orderNumber;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customerEntity;

    @ManyToOne
    @JoinColumn(name = "franchisee_id", nullable = false)
    private FranchiseeEntity franchiseeEntity;

    @OneToOne(mappedBy = "orderEntity")
    private RefundEntity refundEntity;

    @OneToMany(mappedBy = "orderEntity", cascade = CascadeType.PERSIST)
    private List<OrderLineEntity> orderLineEntityList;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employeeEntity;

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
        this.employeeEntity = null;
    }

    public OrderEntity addOrderLine(OrderLineEntity orderLineEntity) {
        this.orderLineEntityList.add(orderLineEntity);
        this.totalAmount = this.initOrderLineAmount();
        this.totalQuantity = this.initOrderLineQuantity();
        this.totalVat = this.initOrderLineVAT();
        return this;
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

    public List<RefundProductInfo> getRefundProductInfoList() {
        return this.orderLineEntityList.stream()
            .map(orderLineEntity -> RefundProductInfo.of(orderLineEntity))
            .collect(Collectors.toList());
    }

    // TODO: 2022/08/02 환급 금액 버림 ex)30000 -> 1901 ->1900
    public String getTotalRefund() {
        double vat = Double.parseDouble(this.totalVat);
        double totalRefund = (int) Math.floor((vat*70)/100);
        int totalRefundResult = (int) (Math.floor(totalRefund/100)) * 100;
        return Integer.toString(totalRefundResult);
    }

    public long getPointsWithPercentage(double balancePercentage){
        double vat = Double.parseDouble(this.totalVat);
        return (long) Math.floor((vat * balancePercentage) / 100);
    }

    public OrderEntity setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public void setRefundEntity(RefundEntity refundEntity) {
        this.refundEntity = refundEntity;
    }

    public void setEmployeeEntity(EmployeeEntity employeeEntity) {
        this.employeeEntity = employeeEntity;
    }
}
