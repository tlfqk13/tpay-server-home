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

    public CustomerRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<CustomerDto.Response> adminFindAll(Pageable pageable, String searchKeyword) {

        List<CustomerDto.Response> content = queryFactory
                .select(new QCustomerDto_Response(
                        customerEntity.passportNumber.substring(0,4),
                        customerEntity.customerName,
                        customerEntity.customerEmail,
                        customerEntity.customerPaymentType,
                        customerEntity.customerCreditNumber,
                        customerEntity.customerBankName,
                        customerEntity.customerAccountNumber,
                        customerEntity.nation
                ))
                .from(orderEntity)
                .leftJoin(orderEntity.customerEntity, customerEntity)
                .where(customerEntity.isRegister.eq(true)
                                .and(customerEntity.customerName.contains(searchKeyword))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .groupBy(customerEntity.passportNumber)
                .orderBy(customerEntity.id.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(orderEntity.count())
                .from(orderEntity)
                .leftJoin(orderEntity.customerEntity, customerEntity);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
