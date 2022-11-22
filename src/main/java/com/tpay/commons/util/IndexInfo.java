package com.tpay.commons.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 향후 index 값을 Long으로 변경하는 게 좋을 듯
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IndexInfo {
    private UserSelector userSelector;
    private Long index;
}
