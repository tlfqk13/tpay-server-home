package com.tpay.domains.push.test.domain;

import com.google.gson.JsonObject;
import com.tpay.domains.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Map;
import java.util.Set;

import static com.tpay.commons.push.PushHistoryStringFormat.*;

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

    private String type;
    private String typeValue;

    private String response;

    public static PushHistoryEntity fromJsonObjectAndResponse(JsonObject jsonObject, String response) {

        Map<String, String> result = pushHistoryStringFormatter(jsonObject, response);

        return PushHistoryEntity.builder()
                .title(result.get("title"))
                .body(result.get("body"))
                .type(result.get("type"))
                .typeValue(result.get("typeValue"))
                .response(result.get("response"))
                .build();
    }
}
