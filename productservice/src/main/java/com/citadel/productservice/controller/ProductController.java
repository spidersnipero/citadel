package com.citadel.productservice.controller;

import com.citadel.productservice.DTO.ProductRequestDTO;
import com.citadel.productservice.DTO.ProductResponseDTO;
import com.citadel.productservice.DTO.UploadImageRequestDTO;
import com.citadel.productservice.DTO.UploadImageResponseDTO;
import com.citadel.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/image-upload-url")
    public ResponseEntity<UploadImageResponseDTO> uploadImage(@Valid  @RequestBody UploadImageRequestDTO requestDTO){
        UploadImageResponseDTO responseDTO = productService.generatePresignedUrlForUpload(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/")
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO requestDTO, Authentication authentication){
        String creatorEmail = authentication.getName();
        ProductResponseDTO newProduct = productService.createProduct(requestDTO,creatorEmail);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "")String [] sort,
            @RequestParam(required = false,name = "search") String searchText
            ){
        try {
            Page<ProductResponseDTO> productList = productService.getAllProducts(page,size,sort,searchText);
            return new ResponseEntity<>(productList, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/product-id/{productId}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable UUID productId){
        ProductResponseDTO responseDTO = productService.getProduct(productId);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductResponseDTO> getProductBySku(@PathVariable String sku){
        ProductResponseDTO responseDTO = productService.getProductBySku(sku);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable UUID productId, @RequestBody ProductRequestDTO requestDTO ) {
            ProductResponseDTO updatedProduct = productService.updateProduct(productId,requestDTO);
            return new ResponseEntity<>(updatedProduct,HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId){
        productService.deleteProduct(productId);
        return  ResponseEntity.noContent().build();
    }
}
