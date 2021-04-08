package com.tpay.domains.refund.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import com.tpay.domains.product.domain.ProductEntity;
import com.tpay.domains.product.domain.ProductRepository;
import com.tpay.domains.sale.domain.SaleEntity;
import com.tpay.domains.sale.domain.SaleLineEntity;
import com.tpay.domains.sale.domain.SaleRepository;

import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles(profiles = {"local", "test"})
class RefundRepositoryTest {

  @Autowired private RefundRepository refundRepository;
  @Autowired private CustomerRepository customerRepository;
  @Autowired private FranchiseeRepository franchiseeRepository;
  @Autowired private SaleRepository saleRepository;
  @Autowired private ProductRepository productRepository;


  List<SaleLineEntity> saleLineEntityList = new LinkedList<>();

  SaleEntity saleEntity;

  @BeforeEach
  public void setUp() {

    FranchiseeEntity franchiseeEntity =
        franchiseeRepository.save(
            FranchiseeEntity.builder()
                .storeTel("00000")
                .storeName("SUCCESSMODE")
                .storeAddress("안양")
                .sellerName("주병천")
                .productCategory("모자")
                .password("0000")
                .businessNumber("0000000")
                .build());

    CustomerEntity customerEntity =
        customerRepository.save(
            CustomerEntity.builder()
                .customerName("AEJEONG SHIN")
                .nation("EN")
                .passportNumber("SW1237674")
                .build());
    ProductEntity productEntity =
        productRepository.save(
            ProductEntity.builder()
                .code("E112")
                .lineNumber("1")
                .name("BAG")
                .price("20000")
                .build());

    saleLineEntityList.add(
        SaleLineEntity.builder().productEntity(productEntity).quantity("4").build());

    saleEntity =
        saleRepository.save(
            SaleEntity.builder()
                .customerEntity(customerEntity)
                .franchiseeEntity(franchiseeEntity)
                .saleLineEntity(saleLineEntityList)
                .build());

    saleRepository.save(saleEntity);
  }
  // TODO: 2021/04/02 조회 JOIN QUERY 부재
  @Test
  public void 환급_생성_테스트() {
    // given
    SaleEntity saleEntity = saleRepository.findAll().stream().findFirst().get();

    RefundEntity refundEntity =
        refundRepository.save(
            RefundEntity.builder()
                .approvalNumber("FDFE2354")
                .refundStatus(RefundStatus.APPROVAL)
                .saleEntity(saleEntity)
                .totalRefund("35000")
                .build());
    // when
    List<RefundEntity> refundEntityList = refundRepository.findAll();

    // then
    assertThat(refundEntityList.stream().findFirst().get(), is(equalTo(refundEntity)));
  }

  @Test
  public void 환급_상태_테스트() {
    // given
    RefundEntity refundEntity =
        RefundEntity.builder().saleEntity(saleEntity).refundStatus(RefundStatus.APPROVAL).build();

    // then
    assertThat(refundEntity.getRefundStatus().toString(), is(equalTo("APPROVAL")));
  }
}
