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

    private Boolean isRead;
    private Boolean isDetail;

    public void updateIsDetail() {
        if (this.pushCategory.equals("3") || this.pushCategory.equals("6")) {
            this.isDetail = true;
        }
    }

    public void updateIsReadInit() {
        if (this.pushCategory.equals("7")|| this.pushCategory.equals("13")|| this.pushCategory.equals("14")) {
            this.isRead = true;
        }
    }

    public void updateIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
}
