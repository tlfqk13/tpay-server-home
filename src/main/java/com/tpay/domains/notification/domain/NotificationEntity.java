package com.tpay.domains.notification.domain;

import com.tpay.commons.custom.CustomValue;
import com.tpay.domains.BaseTimeEntity;
import com.tpay.domains.notification.application.dto.DataList;
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

    private Boolean isInvisible;

    private LocalDateTime scheduledDate;

    private String title;

    private String subTitle1;

    private String subTitle2;

    private String subTitle3;

    private String content1;

    private String content2;

    private String content3;

    // TODO: 2022/05/18 change to ENUM ?
    private String link;

    private String baseS3Uri;

    private String mainImage;

    private String subImg1;

    private String subImg2;

    private String subImg3;

    public NotificationEntity(DataList dataList) {
        this.isFixed = dataList.getIsFixed();
        this.isImmediate = dataList.getIsImmediate();
        this.isInvisible = dataList.getIsInvisible();
        this.scheduledDate = dataList.getScheduledDate().equals("") ? LocalDateTime.now() : LocalDateTime.parse(dataList.getScheduledDate());
        this.title = dataList.getTitle();
        this.subTitle1 = dataList.getSubTitle1();
        this.subTitle2 = dataList.getSubTitle2();
        this.subTitle3 = dataList.getSubTitle3();
        this.content1 = dataList.getContent1();
        this.content2 = dataList.getContent2();
        this.content3 = dataList.getContent3();
        this.link = dataList.getLink();
        this.baseS3Uri = CustomValue.S3_BASE_URI;
        this.mainImage = "";
        this.subImg1 = "";
        this.subImg2 = "";
        this.subImg3 = "";
    }

    public void setImages(String fileName, String path) {
        switch (fileName) {
            case "mainImg":
                this.mainImage = path;
                break;
            case "subImg1":
                this.subImg1 = path;
                break;
            case "subImg2":
                this.subImg2 = path;
                break;
            case "subImg3":
                this.subImg3 = path;
                break;
        }
    }
}
