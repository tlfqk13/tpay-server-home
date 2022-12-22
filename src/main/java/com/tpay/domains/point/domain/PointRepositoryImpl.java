package com.tpay.domains.point.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static com.tpay.domains.point.domain.QPointEntity.pointEntity;
import static com.tpay.domains.point_scheduled.domain.QPointScheduledEntity.pointScheduledEntity;

public class PointRepositoryImpl implements PointRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public PointRepositoryImpl(EntityManager em){this.queryFactory = new JPAQueryFactory(em);}

    @Override
    public Long findDisappearPoints(Long franchiseeId, LocalDateTime disappearDate) {
        return queryFactory.select(pointEntity.withdrawalCheck.sum())
                .from(pointEntity)
                .where(pointEntity.createdDate.before(disappearDate),
                        pointEntity.franchiseeEntity.id.eq(franchiseeId))
                .fetchOne();
    }

    @Override
    public Long findScheduledPoints(Long franchiseeId) {
        return queryFactory.select(pointScheduledEntity.value.sum())
                .from(pointScheduledEntity)
                .where(pointScheduledEntity.pointStatus.eq(PointStatus.SCHEDULED),
                        pointScheduledEntity.franchiseeEntity.id.eq(franchiseeId))
                .fetchOne();
    }

    @Override
    public Long findTotalPoints(Long franchiseeId) {
        return queryFactory.select(pointEntity.balance.sum())
                .from(pointEntity)
                .where(pointEntity.pointStatus.eq(PointStatus.SAVE),
                        pointEntity.franchiseeEntity.id.eq(franchiseeId))
                .fetchOne();
    }
}
