package com.tpay.domains.order.presentation;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.JwtRuntimeException;
import com.tpay.commons.jwt.AuthToken;
import com.tpay.commons.jwt.JwtUtils;
import com.tpay.commons.util.IndexInfo;
import com.tpay.domains.order.application.OrderSaveService;
import com.tpay.domains.order.application.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.tpay.commons.util.KtpCommonUtil.getIndexInfoFromClaims;

@RestController
@Slf4j
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderSaveService orderService;
    private final JwtUtils jwtUtils;

    /**
     * 사후환급 시, 주문을 먼저 만들어야하는 상황에 사용한다
     * 사후환급이지만 한도조회를 먼저 진행해서 여권의 validation 을 진행과 동시에 customer 정보를 등록하고 진행한다
     */
    @PostMapping
    public ResponseEntity<OrderDto.Response> order(HttpServletRequest request, @RequestBody OrderDto.Request orderDto) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(bearerToken)) {
            throw new JwtRuntimeException(ExceptionState.INVALID_TOKEN, "Token Data Empty");
        }
        AuthToken authToken = jwtUtils.convertAuthToken(bearerToken);
        IndexInfo indexInfo = getIndexInfoFromClaims(authToken.getData());
        return ResponseEntity.ok(orderService.createOrder(orderDto, indexInfo));
    }
}
