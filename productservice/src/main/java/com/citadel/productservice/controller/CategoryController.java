package com.citadel.productservice.controller;

import com.citadel.productservice.DTO.CategoryRequestDTO;
import com.citadel.productservice.DTO.CategoryResponseDTO;
import com.citadel.productservice.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;


    @PostMapping("/add")
    public ResponseEntity<CategoryResponseDTO> addCategory(@Valid @RequestBody CategoryRequestDTO requestDTO){
        CategoryResponseDTO responseDTO = categoryService.addCategory(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/tester")
    public String tester(@Valid @RequestBody CategoryRequestDTO requestDTO){
        CategoryResponseDTO responseDTO = categoryService.addCategory(requestDTO);
        return "passed";
    }
}
