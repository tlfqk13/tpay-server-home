package com.tpay.domains.category.presentation;

import com.tpay.domains.category.application.CategoryFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryFindController {

    private final CategoryFindService categoryFindService;

    @GetMapping("/categories")
    public ResponseEntity<String> findAll() {
        //List<CategoryInfo> categoryInfoList = categoryFindService.findAll();
        return ResponseEntity.ok("categories method call");
    }
}
