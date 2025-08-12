package com.citadel.productservice.controller;

import com.citadel.productservice.DTO.CategoryRequestDTO;
import com.citadel.productservice.DTO.CategoryResponseDTO;
import com.citadel.productservice.model.Category;
import com.citadel.productservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<CategoryResponseDTO> addCategory(@RequestBody CategoryRequestDTO requestDTO){
        CategoryResponseDTO responseDTO = categoryService.addCategory(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/tester")
    public String tester(@RequestBody CategoryRequestDTO requestDTO){
        CategoryResponseDTO responseDTO = categoryService.addCategory(requestDTO);
        return "passed";
    }
}
