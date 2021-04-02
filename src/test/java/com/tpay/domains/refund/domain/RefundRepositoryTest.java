package com.tpay.domains.refund.domain;

import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import com.tpay.domains.sale.domain.SaleEntity;
import com.tpay.domains.sale.domain.SaleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
@ActiveProfiles(profiles = {"local", "test"})
class RefundRepositoryTest {

  @Autowired private RefundRepository refundRepository;
  @Autowired private CustomerRepository customerRepository;
  @Autowired private FranchiseeRepository franchiseeRepository;
  @Autowired private SaleRepository saleRepository;

  @BeforeEach
  public void setUp() {

    FranchiseeEntity franchiseeEntity =
        franchiseeRepository.save(FranchiseeEntity.builder().build());

    CustomerEntity customerEntity =
        customerRepository.save(
            CustomerEntity.builder()
                .customerName("AEJEONG SHIN")
                .nation("EN")
                .passportNumber("SW1237674")
                .build());

    SaleEntity saleEntity =
        saleRepository.save(
            SaleEntity.builder()
                .orderNumber("TEST")
                .totalAmount("340000")
                .totalQuantity("1")
                .totalVat("34000")
                .saleDate("TEST")
                .customerEntity(customerEntity)
                .franchiseeEntity(franchiseeEntity)
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
    RefundEntity refundEntity = RefundEntity.builder().refundStatus(RefundStatus.APPROVAL).build();

    // then
    assertThat(refundEntity.getRefundStatus().toString(), is(equalTo("APPROVAL")));
  }
}
