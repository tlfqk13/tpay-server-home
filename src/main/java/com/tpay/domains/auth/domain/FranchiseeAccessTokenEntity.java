package com.tpay.domains.auth.domain;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.JwtRuntimeException;
import com.tpay.domains.BaseTimeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Table(name = "franchisee_accesstoken")
@Entity
public class FranchiseeAccessTokenEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "franchisee_id")
    private FranchiseeEntity franchiseeEntity;

    private String accessToken;

    @Builder
    public FranchiseeAccessTokenEntity(FranchiseeEntity franchiseeEntity, String accessToken) {
        this.franchiseeEntity = franchiseeEntity;
        this.accessToken = accessToken;
    }

    public FranchiseeAccessTokenEntity accessToken(String accessToken){
        this.accessToken = accessToken;
        return this;
    }

    public void validUser(Long parsedIndex) {
        if (!this.franchiseeEntity.getId().equals(parsedIndex)) {
            throw new JwtRuntimeException(ExceptionState.FORCE_REFRESH);
        }
    }

    public boolean validToken(String accessToken) {
        if (!this.accessToken.equals(accessToken)) {
            return true;
        }
        return false;
    }
/*
    public void validToken(String accessToken) {
        if (!this.accessToken.equals(accessToken)) {
            throw new JwtRuntimeException(ExceptionState.FORCE_REFRESH);
        }
    }*/
}
