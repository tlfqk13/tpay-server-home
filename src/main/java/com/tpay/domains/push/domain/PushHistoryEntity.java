package com.tpay.domains.push.domain;

import com.google.gson.JsonObject;
import com.tpay.domains.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Map;

import static com.tpay.commons.push.PushHistoryStringFormat.pushHistoryStringFormatter;

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

    public static PushHistoryEntity fromJsonObjectAndResponse(JsonObject jsonObject, String response) {

        Map<String, String> result = pushHistoryStringFormatter(jsonObject, response);

        return PushHistoryEntity.builder()
                .title(result.get("title"))
                .body(result.get("body"))
                .pushType(result.get("pushType"))
                .pushTypeValue(result.get("pushTypeValue"))
                .response(result.get("response"))
                .pushCategory(result.get("pushCategory"))
                .link(result.get("link"))
                .build();
    }
}
