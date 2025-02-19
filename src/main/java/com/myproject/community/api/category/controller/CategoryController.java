package com.myproject.community.api.category.controller;

import com.myproject.community.api.category.CategoryDto;
import com.myproject.community.api.category.CategoryMainDto;
import com.myproject.community.api.category.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping("/api/categories/top6")
    public ResponseEntity<List<CategoryMainDto>> categoryTop6() {
        return ResponseEntity.ok(categoryService.getMainCategory());
    }
}
