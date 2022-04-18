package com.tpay.domains.push.domain;

import com.tpay.commons.util.UserSelector;
import com.tpay.domains.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "push_history")
@Entity
public class PushHistoryEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String body;

    private String pushType;
    private String pushTypeValue;

    private String pushCategory;
    private String link;

    private String response;

    @Enumerated(EnumType.STRING)
    private UserSelector userSelector;
    private Long userId;
}
