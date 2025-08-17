package com.citadel.inventoryservice.proxy;

import com.citadel.inventoryservice.DTO.ProductResponseDTO;
import com.citadel.inventoryservice.exception.ProductClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "productservice",fallback =  ProductClientFallback.class)
public interface ProductProxy {
    @GetMapping("/products/sku/{sku}")
    ProductResponseDTO getProductBySku(@PathVariable String sku);

}
