package com.citadel.inventoryservice.controller;

import com.citadel.inventoryservice.DTO.InventoryDTO;
import com.citadel.inventoryservice.DTO.ProductResponseDTO;
import com.citadel.inventoryservice.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;


    @PostMapping("/")
    public ResponseEntity<Void> addInventory(@Valid @RequestBody InventoryDTO inventory){
        inventoryService.addInventory(inventory,inventory.getSku());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{sku}")
    public ResponseEntity<InventoryDTO> getInventory(@PathVariable String sku){
        InventoryDTO item = inventoryService.getInventory(sku);
        return new ResponseEntity<>(item,HttpStatus.OK);
    }


    @GetMapping("/product/{sku}")
    public ResponseEntity<ProductResponseDTO> getProductBySku(@PathVariable String sku){
        ProductResponseDTO item = inventoryService.getProductBySku(sku);
        return new ResponseEntity<>(item,HttpStatus.OK);
    }

}
