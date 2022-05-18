package com.tpay.domains.notification.domain;

import com.tpay.domains.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notification")
@Entity
public class NotificationEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isFixed;

    private Boolean isImmediate;

    private LocalDateTime scheduledDate;

    private String title;

    private String subTitle1;

    private String subTitle2;

    private String subTitle3;

    private String content1;

    private String content2;

    private String content3;

    private String link;

    private String mainImage;

    private String subImg1;

    private String subImg2;

    private String subImg3;

}
