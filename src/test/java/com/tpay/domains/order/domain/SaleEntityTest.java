package com.tpay.domains.order.domain;

import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import com.tpay.domains.product.domain.ProductEntity;
import com.tpay.domains.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class SaleEntityTest {

  @Autowired SaleRepository saleRepository;
  @Autowired FranchiseeRepository franchiseeRepository;
  @Autowired CustomerRepository customerRepository;
  @Autowired SaleLineRepository saleLineRepository;
  @Autowired ProductRepository productRepository;
  ProductEntity productEntity;
  CustomerEntity customerEntity;
  FranchiseeEntity franchiseeEntity;

  String businessNumber = ("123-45-67890").replace("-", "");
  String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

  List<OrderLineEntity> orderLineEntityList = new LinkedList<>();

  @BeforeEach
  public void setUp() {
    customerEntity =
        customerRepository.save(
            CustomerEntity.builder()
                .customerName("success")
                .nation("ko")
                .passportNumber("3489384")
                .build());

    franchiseeEntity =
        franchiseeRepository.save(
            FranchiseeEntity.builder()
                .storeTel("00000")
                .storeName("SUCCESSMODE")
                .storeAddress("안양")
                .sellerName("주병천")
                .productCategory("모자")
                .password("0000")
                .businessNumber(businessNumber)
                .build());

    productEntity =
        productRepository.save(
            ProductEntity.builder()
                .code("E112")
                .lineNumber("1")
                .name("BAG")
                .price("20000")
                .build());

    orderLineEntityList.add(
        OrderLineEntity.builder().productEntity(productEntity).quantity("4").build());
  }

  @Test
  public void 구매_상품_총_갯수_계산_테스트() {

    // given
    ProductEntity productEntity =
        productRepository.save(
            ProductEntity.builder()
                .code("E113")
                .lineNumber("2")
                .name("OTHER")
                .price("19000")
                .build());

    orderLineEntityList.add(
        OrderLineEntity.builder().productEntity(productEntity).quantity("1").build());
    // when
    OrderEntity orderEntity =
        saleRepository.save(
            OrderEntity.builder()
                .saleLineEntity(orderLineEntityList)
                .customerEntity(customerEntity)
                .franchiseeEntity(franchiseeEntity)
                .build());
    // then
    assertThat(orderEntity.getTotalQuantity(), is("5"));
  }

  @Test
  public void 구매_상품_총_금액_계산_테스트() {

    // given
    ProductEntity productEntity =
        productRepository.save(
            ProductEntity.builder()
                .code("E113")
                .lineNumber("2")
                .name("PANTS")
                .price("215000")
                .build());

    orderLineEntityList.add(
        OrderLineEntity.builder().productEntity(productEntity).quantity("1").build());

    // when
    OrderEntity orderEntity =
        saleRepository.save(
            OrderEntity.builder()
                .saleLineEntity(orderLineEntityList)
                .customerEntity(customerEntity)
                .franchiseeEntity(franchiseeEntity)
                .build());
    // then
    assertThat(orderEntity.getTotalAmount(), is("295000"));
  }

  @Test
  public void 구매_상품_총_VAT_계산_테스트() {
    // given
    ProductEntity productEntity =
        productRepository.save(
            ProductEntity.builder()
                .code("E113")
                .lineNumber("2")
                .name("SOCKS")
                .price("20000")
                .build());

    orderLineEntityList.add(
        OrderLineEntity.builder().productEntity(productEntity).quantity("5").build());

    // when
    OrderEntity orderEntity =
        saleRepository.save(
            OrderEntity.builder()
                .franchiseeEntity(franchiseeEntity)
                .customerEntity(customerEntity)
                .saleLineEntity(orderLineEntityList)
                .build());

    // then
    assertThat(orderEntity.getTotalVat(), is("16362"));
  }

  @Test
  public void 주문번호_생성_테스트() {

    saleLineRepository.save(orderLineEntityList.stream().findFirst().get());
    // when
    OrderEntity orderEntity =
        saleRepository.save(
            OrderEntity.builder()
                .saleLineEntity(orderLineEntityList)
                .customerEntity(customerEntity)
                .franchiseeEntity(franchiseeEntity)
                .build());

    // then
    System.out.println("TEST orderNumber : " + orderEntity.getOrderNumber());
    assertTrue(orderEntity.getOrderNumber().length() <= 16);
  }

  @Test
  public void 구매_날짜_생성_테스트() {

    OrderEntity orderEntity = OrderEntity.builder().saleLineEntity(orderLineEntityList).build();
    saleLineRepository.save(
        OrderLineEntity.builder()
            .productEntity(productEntity)
            .quantity("4")
            .saleEntity(orderEntity)
            .build());
    System.out.println(orderEntity.getSaleDate());
    assertThat(orderEntity.getSaleDate(), startsWith(now.substring(0, 11)));
  }
}
