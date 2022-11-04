package com.tpay.domains.franchisee_applicant.domain;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantDto;
import com.tpay.domains.franchisee_applicant.application.dto.QFranchiseeApplicantDto_Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.tpay.domains.franchisee.domain.QFranchiseeEntity.franchiseeEntity;
import static com.tpay.domains.franchisee_applicant.domain.QFranchiseeApplicantEntity.franchiseeApplicantEntity;

public class FranchiseeApplicantImpl implements FranchiseeApplicantCustom {

    private final JPAQueryFactory queryFactory;

    public FranchiseeApplicantImpl(EntityManager em){this.queryFactory = new JPAQueryFactory(em);}
    @Override
    public Page<FranchiseeApplicantDto.Response> filterAndBusinessNumberDsl(List<Boolean> booleanList
            , List<FranchiseeStatus> franchiseeStatusList
            , Pageable pageable, String searchKeyword) {

        List<FranchiseeApplicantDto.Response> content = queryFactory
                .select(new QFranchiseeApplicantDto_Response(
                        franchiseeApplicantEntity.id,
                        franchiseeApplicantEntity.franchiseeStatus,
                        franchiseeEntity.businessNumber,
                        franchiseeEntity.storeName,
                        franchiseeEntity.sellerName,
                        franchiseeEntity.createdDate,
                        franchiseeEntity.isRefundOnce,
                        franchiseeApplicantEntity.isRead
                ))
                .from(franchiseeApplicantEntity)
                .leftJoin(franchiseeEntity).on(franchiseeApplicantEntity.id.eq(franchiseeEntity.id))
                .where(franchiseeEntity.storeName.like(searchKeyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(franchiseeEntity.id.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(franchiseeApplicantEntity.count())
                .from(franchiseeApplicantEntity)
                .leftJoin(franchiseeEntity).on(franchiseeApplicantEntity.id.eq(franchiseeEntity.id));


        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
