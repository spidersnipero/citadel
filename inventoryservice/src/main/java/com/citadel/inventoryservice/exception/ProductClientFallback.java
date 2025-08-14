package com.citadel.inventoryservice.exception;

import com.citadel.inventoryservice.DTO.ProductResponseDTO;
import com.citadel.inventoryservice.proxy.ProductProxy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductClientFallback implements ProductProxy {

    @Override
    public ProductResponseDTO getProductBySku(String sku) {
        // Log and return a safe default object
        System.err.println("⚠️ Product Service unavailable or SKU not found: " + sku);
        return null; // Or create a placeholder DTO
    }
}

