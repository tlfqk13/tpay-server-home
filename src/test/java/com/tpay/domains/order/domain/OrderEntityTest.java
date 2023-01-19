package com.tpay.domains.order.domain;

import com.tpay.commons.aria.ARIAEngine;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import com.tpay.domains.product.domain.ProductEntity;
import com.tpay.domains.product.domain.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;

@DataJpaTest
@ActiveProfiles(profiles = {"test"})
class OrderEntityTest {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OrderLineRepository orderLineRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FranchiseeRepository franchiseeRepository;

    @Test
    void getPointsWithPercentage() throws UnsupportedEncodingException, InvalidKeyException {

        String passportNumber = ARIAEngine.encrypt("SUCCESS08");

        FranchiseeEntity franchiseeEntity = FranchiseeEntity.builder()
                .businessNumber("2141582141")
                .storeName("SUCCESS")
                .storeAddressNumber("addressNumber")
                .storeAddressBasic("basic")
                .storeAddressDetail("detail")
                .sellerName("seller")
                .storeTel("tel")
                .productCategory("category")
                .password("password")
                .signboard("signboard")
                .storeNumber("storenumber")
                .email("email")
                .isTaxRefundShop("Y")
                .balancePercentage(0.33)
                .build();

        franchiseeRepository.save(franchiseeEntity);

        System.out.println(franchiseeEntity.getId());
        System.out.println(franchiseeEntity.getBalancePercentage());

        CustomerEntity customerEntity = CustomerEntity.builder()
                .passportNumber(passportNumber)
                .customerName("SUCCESS08")
                .nation("USA")
                .build();
        customerRepository.save(customerEntity);

        OrderEntity orderEntity = OrderEntity.builder()
                .customerEntity(customerEntity)
                .franchiseeEntity(franchiseeEntity)
                .purchaseSn("1234")
                .build();
        orderRepository.save(orderEntity);

        ProductEntity productEntity = ProductEntity.builder()
                .name("name")
                .lineNumber("1")
                .code("code")
                .price("54000")
                .refund("3000")
                .build();

        productRepository.save(productEntity);

        OrderLineEntity orderLineEntity = OrderLineEntity.builder()
                .quantity("1")
                .orderEntity(orderEntity)
                .productEntity(productEntity)
                .build();

        orderLineRepository.save(orderLineEntity);

        orderEntity.addOrderLine(orderLineEntity);

        double vat = Double.parseDouble(orderEntity.getTotalVat());
        double refund = Double.parseDouble(orderEntity.getTotalRefund());
        double earn = vat-refund;
        double balance = franchiseeEntity.getBalancePercentage();
        long point = Math.round((vat-refund) * balance);

        System.out.println(vat);
        System.out.println(refund);
        System.out.println(earn);
        System.out.println(balance);
        System.out.println(point);
    }
}