package com.tpay.domains.point.domain;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tpay.domains.point.application.dto.AdminPointInfo;
import com.tpay.domains.point.application.dto.QAdminPointInfo;
import com.tpay.domains.point.application.dto.WithdrawalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.tpay.domains.franchisee.domain.QFranchiseeEntity.franchiseeEntity;
import static com.tpay.domains.point.domain.QPointEntity.pointEntity;

public class PointRepositoryImpl implements PointRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PointRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<AdminPointInfo> findPointsAdmin(Pageable pageable, WithdrawalStatus withdrawalStatus
            , String searchKeyword, boolean isBusinessNumber, Boolean isAll) {

        List<AdminPointInfo> content = queryFactory
                .select(new QAdminPointInfo(
                        pointEntity.id,
                        pointEntity.pointStatus,
                        pointEntity.franchiseeEntity.businessNumber,
                        pointEntity.franchiseeEntity.storeName,
                        pointEntity.franchiseeEntity.sellerName,
                        pointEntity.createdDate.stringValue().substring(0, 10),
                        pointEntity.change,
                        pointEntity.isRead
                ))
                .from(pointEntity)
                .innerJoin(pointEntity.franchiseeEntity, franchiseeEntity)
                .where(isWithdrawalStatus(withdrawalStatus)
                        .and(isKeyword(isBusinessNumber, searchKeyword))
                        .and(isAll(isAll)))
                .orderBy(pointEntity.createdDate.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(pointEntity.count())
                .from(pointEntity)
                .innerJoin(pointEntity.franchiseeEntity, franchiseeEntity)
                .where(isWithdrawalStatus(withdrawalStatus)
                        .and(isKeyword(isBusinessNumber, searchKeyword))
                        .and(isAll(isAll)))
                .orderBy(pointEntity.createdDate.desc());

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression isWithdrawalStatus(WithdrawalStatus withdrawalStatus) {
        if(WithdrawalStatus.WITHDRAW.equals(withdrawalStatus)){
            return pointEntity.pointStatus.in(PointStatus.WITHDRAW);
        }else if (WithdrawalStatus.COMPLETE.equals(withdrawalStatus)){
            return pointEntity.pointStatus.in(PointStatus.COMPLETE);
        }else {
            return pointEntity.pointStatus.in(PointStatus.WITHDRAW,PointStatus.COMPLETE);
        }
    }

    private BooleanExpression isAll(boolean isAll) {
        if(!isAll){
            return pointEntity.isRead.eq(false);
        }else{
            return null;
        }
    }

    private BooleanExpression isKeyword(Boolean businessNumber, String keyword) {
        if (businessNumber) {
            return franchiseeEntity.businessNumber.contains(keyword);
        } else {
            return franchiseeEntity.storeName.contains(keyword);
        }
    }
}
