package com.tpay.domains.push.domain;

import com.tpay.commons.util.UserSelector;
import com.tpay.domains.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "user_push_token")
@Entity
public class UserPushTokenEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_selector")
    private UserSelector userSelector;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_token")
    private String userToken;

    public void updateToken(String userToken) {
        this.userToken = userToken;
    }


}
