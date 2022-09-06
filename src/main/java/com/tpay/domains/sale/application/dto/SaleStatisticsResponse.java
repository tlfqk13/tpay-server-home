package com.tpay.domains.sale.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class SaleStatisticsResponse {
    SaleStatisticsCurrentResponse saleStatisticsCurrentResponse;
    SaleStatisticsPreviousResponse saleStatisticsPreviousResponse;
}
