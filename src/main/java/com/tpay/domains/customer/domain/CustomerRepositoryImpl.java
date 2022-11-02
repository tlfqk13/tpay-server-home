package com.tpay.domains.customer.domain;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tpay.domains.customer.application.dto.CustomerDto;
import com.tpay.domains.customer.application.dto.QCustomerDto_Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.tpay.domains.customer.domain.QCustomerEntity.customerEntity;
import static com.tpay.domains.order.domain.QOrderEntity.orderEntity;

public class CustomerRepositoryImpl implements CustomerRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CustomerRepositoryImpl(EntityManager em){this.queryFactory = new JPAQueryFactory(em);}

    @Override
    public Page<CustomerDto.Response> adminFindAll(Pageable pageable, String searchKeyword) {

       List<CustomerDto.Response> content = queryFactory
                .select(new QCustomerDto_Response(
                        customerEntity.passportNumber,
                        customerEntity.customerName,
                        customerEntity.customerEmail,
                        customerEntity.customerPaymentType,
                        customerEntity.customerCreditNumber,
                        customerEntity.customerBankName,
                        customerEntity.customerAccountNumber,
                        customerEntity.nation
                ))
               .from(customerEntity)
               .leftJoin(orderEntity.customerEntity,customerEntity)
               .where(customerEntity.customerName.eq(searchKeyword)
                       .and(customerEntity.isRegister))
               .offset(pageable.getOffset())
               .limit(pageable.getPageSize())
               .orderBy(customerEntity.id.desc())
               .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(customerEntity.count())
                .from(customerEntity);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
