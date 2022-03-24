package com.tpay.domains.category.presentation;

import com.tpay.domains.category.application.CategoryFindService;
import com.tpay.domains.category.application.dto.CategoryInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryFindController {

    private final CategoryFindService categoryFindService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryInfo>> findAll() {
        List<CategoryInfo> categoryInfoList = categoryFindService.findAll();
        return ResponseEntity.ok(categoryInfoList);
    }
}
