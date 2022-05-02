package com.tpay.domains.pos.presentation;

import com.tpay.domains.pos.application.PosService;
import com.tpay.domains.pos.domain.UpdatePosTypeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PosController {

    private final PosService posService;

    @PatchMapping("/pos/{franchiseeIndex}")
    public ResponseEntity<Boolean> updatePosType(
        @PathVariable Long franchiseeIndex,
        @RequestBody UpdatePosTypeDto.Request updatePosTypeDto){

        Boolean result = posService.updatePosType(franchiseeIndex,updatePosTypeDto);
        return ResponseEntity.ok(result);
    }
}
