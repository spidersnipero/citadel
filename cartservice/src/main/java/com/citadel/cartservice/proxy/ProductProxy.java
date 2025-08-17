package com.citadel.cartservice.proxy;

import com.citadel.cartservice.DTO.ProductResponseDTO;
import com.citadel.cartservice.exception.ProductClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "productservice",fallback = ProductClientFallback.class)
public interface ProductProxy {
    @GetMapping("/products/product-id/{productId}")
    ProductResponseDTO getProductById(@PathVariable("productId") UUID productId);

}
