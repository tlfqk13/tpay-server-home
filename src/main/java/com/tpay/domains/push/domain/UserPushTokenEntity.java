package com.tpay.domains.push.domain;

import com.tpay.commons.util.UserSelector;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "user_push_token")
@Entity
public class UserPushTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "USER_TYPE")
    private UserSelector userType;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "USER_TOKEN")
    private String userToken;

/*
    @Column(name = "BIZ_NO")
    private String businessNumber;
*/


}
