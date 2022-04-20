package com.tpay.domains.push.application.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CountIsReadDto {
    private Long count;
}
