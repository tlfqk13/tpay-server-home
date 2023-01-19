package com.tpay.domains.refund.application;

import com.tpay.commons.aria.ARIAEngine;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.order.domain.OrderLineEntity;
import com.tpay.domains.order.domain.OrderLineRepository;
import com.tpay.domains.order.domain.OrderRepository;
import com.tpay.domains.product.domain.ProductEntity;
import com.tpay.domains.product.domain.ProductRepository;
import com.tpay.domains.refund.application.dto.RefundReceiptDto;
import com.tpay.domains.refund.domain.RefundAfterEntity;
import com.tpay.domains.refund.domain.RefundAfterRepository;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.util.List;
import java.util.stream.Collectors;

@DataJpaTest
@ActiveProfiles(profiles = {"test"})
class RefundReceiptFindServiceTest {

    @Autowired
    private RefundRepository refundRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private FranchiseeRepository franchiseeRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OrderLineRepository orderLineRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RefundAfterRepository refundAfterRepository;

    @Test
    void findRefundReceiptDetail() throws UnsupportedEncodingException, InvalidKeyException {

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
                .balancePercentage(0.1)
                .build();

        franchiseeRepository.save(franchiseeEntity);

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
                .price("1200000")
                .refund("80000")
                .build();

        productRepository.save(productEntity);

        OrderLineEntity orderLineEntity = OrderLineEntity.builder()
                .quantity("1")
                .orderEntity(orderEntity)
                .productEntity(productEntity)
                .build();

        orderLineRepository.save(orderLineEntity);

        orderEntity.addOrderLine(orderLineEntity);

        RefundEntity refundEntity = RefundEntity.builder()
                .responseCode("0000")
                .orderNumber(orderEntity.getOrderNumber())
                .takeOutNumber("A        ")
                .orderEntity(orderEntity)
                .build();
        refundRepository.save(refundEntity);

        RefundAfterEntity refundAfterEntity = RefundAfterEntity.builder()
                .cusCode("cuscode")
                .locaCode("locacode")
                .kioskBsnmCode("ki")
                .kioskCode("kicode")
                .cityRefundCenterCode("city")
                .approvalFinishDate("")
                .build();
        refundAfterRepository.save(refundAfterEntity);

        refundEntity.addRefundAfterEntity(refundAfterEntity);

        List<RefundReceiptDto.Response> response;
        // 최신순, 과거순
        response = refundRepository.findRefundReceipt(customerEntity.getPassportNumber(),true);

        if(!"KOR".equals(customerEntity.getNation())){
            response = response.stream().filter(r -> Integer.parseInt(r.getTotalRefund()) >= 75000).collect(Collectors.toList());
            response.forEach(r-> System.out.println("____!KOR___" + r.getTotalRefund()));
            Assertions.assertThat(response.size()).isEqualTo(1);
        }else {
            response.forEach(r-> System.out.println("____" + r.getTotalRefund()));
        }

        System.out.println((" @@ response.size() --> " +  response.size()));

    }
}