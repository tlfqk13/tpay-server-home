package com.tpay.domains.van.presentation;

import com.tpay.domains.van.application.VanService;
import com.tpay.domains.van.domain.dto.VanOrdersDto;
import com.tpay.domains.van.domain.dto.VanRefundAfterBaseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/van")
@Slf4j
public class VanOrderController {

    private final VanService vanService;


    @PostMapping("/order")
    public ResponseEntity<VanOrdersDto.Response> ordersDetail(
            @RequestBody VanOrdersDto.Request request
    ) {
        log.trace(" @@ request.getRefundAfterBaseDto = {}", request.getRefundAfterBaseDto());
        log.trace(" @@ request.getEncryptPassportNumber = {}", request.getEncryptPassportNumber());
        VanRefundAfterBaseDto refundAfterBaseDto = request.getRefundAfterBaseDto();
        // VAN 의 경우 환급 정보 생성 후 조회
        vanService.createRefundAfter(request.getEncryptPassportNumber(), refundAfterBaseDto);
        //조회
        // TODO: 2022/11/04 VAN 여권번호.decode -> SUCCESS15 || tpay 에서 다시 encrypt 해야함 
        VanOrdersDto.Response response = vanService.findVanOrder(request.getEncryptPassportNumber());
        return ResponseEntity.ok(response);
    }
}
