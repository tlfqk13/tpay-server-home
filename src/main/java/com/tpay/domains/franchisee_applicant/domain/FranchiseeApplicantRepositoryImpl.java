package com.tpay.domains.franchisee_applicant.domain;

import com.querydsl.core.types.dsl.BooleanExpression;
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

public class FranchiseeApplicantRepositoryImpl implements FranchiseeApplicantRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public FranchiseeApplicantRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<FranchiseeApplicantDto.Response> findFranchiseeAllFilter(Pageable pageable, String searchKeyword
            , FranchiseeStatus franchiseeStatus, boolean isRead, boolean isBusinessNumber) {
        List<FranchiseeApplicantDto.Response> content = queryFactory
                .select(new QFranchiseeApplicantDto_Response(
                        franchiseeApplicantEntity.id,
                        franchiseeApplicantEntity.franchiseeStatus,
                        franchiseeEntity.businessNumber,
                        franchiseeEntity.storeName,
                        franchiseeEntity.sellerName,
                        franchiseeEntity.createdDate,
                        franchiseeEntity.isRefundOnce,
                        franchiseeApplicantEntity.isRead,
                        franchiseeEntity.refundStep
                ))
                .from(franchiseeApplicantEntity)
                .leftJoin(franchiseeEntity).on(franchiseeApplicantEntity.id.eq(franchiseeEntity.id))
                .where((isKeyword(isBusinessNumber, searchKeyword))
                        .and(isFranchiseeCondition(franchiseeStatus, isRead)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(franchiseeEntity.id.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(franchiseeApplicantEntity.count())
                .from(franchiseeApplicantEntity)
                .leftJoin(franchiseeEntity).on(franchiseeApplicantEntity.id.eq(franchiseeEntity.id))
                .where((isKeyword(isBusinessNumber, searchKeyword))
                        .and(isFranchiseeCondition(franchiseeStatus, isRead)))
                .orderBy(franchiseeEntity.id.desc());


        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<FranchiseeApplicantDto.Response> findFranchiseeAllFilter(Pageable pageable, String searchKeyword, boolean isRead, boolean isBusinessNumber) {
        List<FranchiseeApplicantDto.Response> content = queryFactory
                .select(new QFranchiseeApplicantDto_Response(
                        franchiseeApplicantEntity.id,
                        franchiseeApplicantEntity.franchiseeStatus,
                        franchiseeEntity.businessNumber,
                        franchiseeEntity.storeName,
                        franchiseeEntity.sellerName,
                        franchiseeEntity.createdDate,
                        franchiseeEntity.isRefundOnce,
                        franchiseeApplicantEntity.isRead,
                        franchiseeEntity.refundStep
                ))
                .from(franchiseeApplicantEntity)
                .leftJoin(franchiseeEntity).on(franchiseeApplicantEntity.id.eq(franchiseeEntity.id))
                .where((isKeyword(isBusinessNumber, searchKeyword))
                        .and(franchiseeApplicantEntity.isRead.eq(isRead)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(franchiseeEntity.id.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(franchiseeApplicantEntity.count())
                .from(franchiseeApplicantEntity)
                .leftJoin(franchiseeEntity).on(franchiseeApplicantEntity.id.eq(franchiseeEntity.id))
                .where((isKeyword(isBusinessNumber, searchKeyword))
                        .and(franchiseeApplicantEntity.isRead.eq(isRead)))
                .orderBy(franchiseeEntity.id.desc());

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<FranchiseeApplicantDto.Response> findFranchiseeAll(Pageable pageable, String searchKeyword, boolean isBusinessNumber) {
        List<FranchiseeApplicantDto.Response> content = queryFactory
                .select(new QFranchiseeApplicantDto_Response(
                        franchiseeApplicantEntity.id,
                        franchiseeApplicantEntity.franchiseeStatus,
                        franchiseeEntity.businessNumber,
                        franchiseeEntity.storeName,
                        franchiseeEntity.sellerName,
                        franchiseeEntity.createdDate,
                        franchiseeEntity.isRefundOnce,
                        franchiseeApplicantEntity.isRead,
                        franchiseeEntity.refundStep
                ))
                .from(franchiseeApplicantEntity)
                .leftJoin(franchiseeEntity).on(franchiseeApplicantEntity.id.eq(franchiseeEntity.id))
                .where((isKeyword(isBusinessNumber, searchKeyword)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(franchiseeEntity.id.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(franchiseeApplicantEntity.count())
                .from(franchiseeApplicantEntity)
                .leftJoin(franchiseeEntity).on(franchiseeApplicantEntity.id.eq(franchiseeEntity.id))
                .where((isKeyword(isBusinessNumber, searchKeyword)))
                .orderBy(franchiseeEntity.id.desc());

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression isKeyword(Boolean businessNumber, String keyword) {
        if (businessNumber) {
            return franchiseeEntity.businessNumber.contains(keyword);
        } else {
            return franchiseeEntity.storeName.contains(keyword);
        }
    }

    private BooleanExpression isFranchiseeCondition(FranchiseeStatus franchiseeStatus, boolean isRead) {
        if (isRead) {
            return franchiseeApplicantEntity.franchiseeStatus.in(franchiseeStatus);
        } else {
            return franchiseeApplicantEntity.franchiseeStatus.in(franchiseeStatus)
                    .and(franchiseeApplicantEntity.isRead.eq(false));
        }
    }
}
