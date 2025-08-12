package com.citadel.productservice.controller;

import com.citadel.productservice.DTO.ProductRequestDTO;
import com.citadel.productservice.DTO.ProductResponseDTO;
import com.citadel.productservice.DTO.UploadImageRequestDTO;
import com.citadel.productservice.DTO.UploadImageResponseDTO;
import com.citadel.productservice.config.AWSS3BucketConfig;
import com.citadel.productservice.model.Product;
import com.citadel.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.List;
import java.util.UUID;

@RestController
@PreAuthorize("hasAnyRole('SELLER','ADMIN')")
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/image-upload-url")
    public ResponseEntity<UploadImageResponseDTO> uploadImage(@RequestBody UploadImageRequestDTO requestDTO){

        try {
            UploadImageResponseDTO responseDTO = productService.generatePresignedUrlForUpload(requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/")
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequestDTO requestDTO){
        try {
            Product newProduct = productService.createProduct(requestDTO);
            return new ResponseEntity<>(newProduct, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts(){
        try {
            List<ProductResponseDTO> productList = productService.getAllProducts();
            return new ResponseEntity<>(productList, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




}
