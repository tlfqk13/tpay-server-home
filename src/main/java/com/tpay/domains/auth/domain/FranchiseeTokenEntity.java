package com.tpay.domains.auth.domain;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.JwtRuntimeException;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "franchisee_token")
@Entity
public class FranchiseeTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "franchisee_id")
    private FranchiseeEntity franchiseeEntity;

    private String refreshToken;

    @Builder
    public FranchiseeTokenEntity(FranchiseeEntity franchiseeEntity, String refreshToken) {
        this.franchiseeEntity = franchiseeEntity;
        this.refreshToken = refreshToken;
    }

    public FranchiseeTokenEntity refreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public void validUser(Long parsedIndex) {
        if (!this.franchiseeEntity.getId().equals(parsedIndex)) {
            throw new JwtRuntimeException(ExceptionState.FORCE_REFRESH);
        }
    }

    public void validToken(String refreshToken) {
        if (!this.refreshToken.equals(refreshToken)) {
            throw new JwtRuntimeException(ExceptionState.FORCE_REFRESH);
        }
    }
}
