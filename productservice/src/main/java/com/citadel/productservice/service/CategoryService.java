package com.citadel.productservice.service;

import com.citadel.productservice.DTO.CategoryRequestDTO;
import com.citadel.productservice.DTO.CategoryResponseDTO;
import com.citadel.productservice.Repo.CategoryRepo;
import com.citadel.productservice.model.Category;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    CategoryRepo categoryRepo;

    @Transactional
    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    public CategoryResponseDTO addCategory(CategoryRequestDTO requestDTO){
            Category newCategory = new Category();
            if(requestDTO.getName()==null) {
                throw new RuntimeException("Category name is required ");
            }
            newCategory.setName(requestDTO.getName());
            if(requestDTO.getParentId()!=null){
                Category parentCategory = categoryRepo.findById(requestDTO.getParentId()).orElseThrow(()->new RuntimeException("Parent category not found with the id provided"));
                newCategory.setParent(parentCategory);
            }
            Category savedCategory = categoryRepo.save(newCategory);
            return convertToResponseDTO(newCategory);
    }

    private CategoryResponseDTO convertToResponseDTO(Category category){
        if(category==null) return null;
        CategoryResponseDTO responseDTO = new CategoryResponseDTO();
        responseDTO.setId(category.getId());
        responseDTO.setName(category.getName());
        if (category.getParent() != null) {
            responseDTO.setParentId(category.getParent().getId());
        }

        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            responseDTO.setChildren(
                    category.getChildren().stream()
                            .map(this::convertToResponseDTO)
                            .collect(Collectors.toList())
            );
        } else {
            responseDTO.setChildren(new ArrayList<>());
        }

        return responseDTO;
    }


}
