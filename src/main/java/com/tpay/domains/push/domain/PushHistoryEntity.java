package com.tpay.domains.push.domain;

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

    private Long userId;

    private Boolean isRead;
    private Boolean isDetail;

    private Long noticeIndex;

    public void updateIsDetail() {
        if (this.pushCategory.equals("13") || this.pushCategory.equals("14")) {
            this.isDetail = false;
        }
    }

    public void updateIsReadInit() {
        if (this.pushCategory.equals("7") || this.pushCategory.equals("13") || this.pushCategory.equals("14")) {
            this.isRead = true;
        }
    }

    public void updateIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public void updateNoticeIndex(Long noticeIndex) {
        this.noticeIndex = noticeIndex;
    }
}
