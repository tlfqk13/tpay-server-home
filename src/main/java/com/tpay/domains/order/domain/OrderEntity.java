package com.tpay.domains.order.domain;

import com.tpay.domains.BaseTimeEntity;
import com.tpay.domains.barcode.domain.BarcodeEntity;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.employee.domain.EmployeeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund_core.application.dto.RefundProductInfo;
import lombok.*;

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
@AllArgsConstructor
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

    @Column(name = "totRefund", length = 7)
    private String totalRefund;

    @Column(name = "purchsSn", length = 20)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customerEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "franchisee_id", nullable = false)
    private FranchiseeEntity franchiseeEntity;

    @OneToOne(mappedBy = "orderEntity", cascade = CascadeType.REMOVE)
    private RefundEntity refundEntity;

    @OneToMany(mappedBy = "orderEntity", cascade = CascadeType.ALL)
    private List<OrderLineEntity> orderLineEntityList;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employeeEntity;

    @OneToOne
    @JoinColumn(name = "barcode_id")
    private BarcodeEntity barcodeEntity;

    @Builder
    public OrderEntity(CustomerEntity customerEntity, FranchiseeEntity franchiseeEntity, String purchaseSn) {
        this.customerEntity = customerEntity;
        this.franchiseeEntity = franchiseeEntity;
        this.orderLineEntityList = new LinkedList<>();
        this.orderNumber = purchaseSn;
        this.initialize();
    }

    public void initialize() {
        this.saleDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        this.employeeEntity = null;
    }

    public OrderEntity addOrderLine(OrderLineEntity orderLineEntity) {
        this.orderLineEntityList.add(orderLineEntity);
        this.totalAmount = this.initOrderLineAmount();
        this.totalQuantity = this.initOrderLineQuantity();
        this.totalVat = this.initOrderLineVAT();
        this.totalRefund = this.initOrderLineRefund();
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

    public String initOrderLineRefund() {
        Long refund =
                this.orderLineEntityList.stream()
                        .map(orderLine -> Long.parseLong(orderLine.getRefund()))
                        .reduce(Long::sum)
                        .orElseGet(() -> 0L);
        return String.valueOf(refund);
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
    public long getPointsWithPercentage(double balancePercentage){
        double vat = Double.parseDouble(this.totalVat);
        return (long) Math.floor((vat * balancePercentage) / 100);
/*        double refund = Double.parseDouble(this.totalRefund);
        return Math.round((vat-refund) * balancePercentage);*/
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

    public void addBarcode(BarcodeEntity barcode){this.barcodeEntity = barcode;}

    public void updateCustomer(CustomerEntity customerEntity){this.customerEntity = customerEntity;}
}
