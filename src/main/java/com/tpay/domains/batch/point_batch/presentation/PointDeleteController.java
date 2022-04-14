package com.tpay.domains.batch.point_batch.presentation;


import com.tpay.domains.batch.point_batch.application.PointDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PointDeleteController {

    private final PointDeleteService pointDeleteService;

    @DeleteMapping("/points/delete")
    public ResponseEntity<String> deletePoint() {
        String b = pointDeleteService.deletePoint();
        return ResponseEntity.ok(b);
    }
}
