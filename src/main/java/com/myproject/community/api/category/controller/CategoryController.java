package com.myproject.community.api.category.controller;

import com.myproject.community.api.category.dto.CategoryDto;
import com.myproject.community.api.category.dto.CategoryMainDto;
import com.myproject.community.api.category.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping("/api/categories/top6")
    public ResponseEntity<List<CategoryMainDto>> categoryTop6() {
        return ResponseEntity.ok(categoryService.getMainCategory());
    }

    @PostMapping("/api/admin/category")
    public ResponseEntity<?> createCategory(@RequestBody CategoryDto categoryDto) {
        categoryService.saveRootCategory(categoryDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/admin/category")
    public ResponseEntity<?> updateCategory(@RequestParam long id, @RequestBody CategoryDto categoryDto) {
        categoryService.updateCategory(id, categoryDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/admin/category")
    public ResponseEntity<?> deleteCategory(@RequestParam long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}
