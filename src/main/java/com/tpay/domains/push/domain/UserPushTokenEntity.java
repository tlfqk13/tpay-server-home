package com.tpay.domains.push.domain;

import com.tpay.domains.BaseTimeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_push_token")
@Entity
public class UserPushTokenEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "franchisee_id")
    private FranchiseeEntity franchiseeEntity;

    @ManyToOne
    @JoinColumn(name = "token_id")
    private PushTokenEntity pushTokenEntity;

    @Builder
    public UserPushTokenEntity(FranchiseeEntity franchiseeEntity, PushTokenEntity pushTokenEntity) {
        this.franchiseeEntity = franchiseeEntity;
        this.pushTokenEntity = pushTokenEntity;
    }
}
