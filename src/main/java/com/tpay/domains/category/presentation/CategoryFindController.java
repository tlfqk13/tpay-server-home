package com.tpay.domains.category.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryFindController {

    @GetMapping("/categories")
    public ResponseEntity<String> findAll() {
        return ResponseEntity.ok("categories method call");
    }
}
