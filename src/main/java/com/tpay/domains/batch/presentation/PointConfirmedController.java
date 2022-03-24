package com.tpay.domains.batch.presentation;


import com.tpay.domains.batch.application.PointConfirmedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PointConfirmedController {

    private final PointConfirmedService pointConfirmedService;

    @GetMapping("/points/batch")
    public ResponseEntity<String> pointUpdater() {
        String s = pointConfirmedService.updateStatus();
        return ResponseEntity.ok(s);
    }

}
