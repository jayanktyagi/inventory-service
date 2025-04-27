package com.example.inventory_service.controller;

import com.example.inventory_service.dto.StockManagementDto;
import com.example.inventory_service.service.StockManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stockManagementService/v1/stockManagement/")
public class StockManagementController {
    StockManagementService stockManagementService;

    @Autowired
    public StockManagementController(StockManagementService stockManagementService) {
        this.stockManagementService = stockManagementService;
    }

    @PostMapping("/increase/{productId}")
    public StockManagementDto decreaseLevel(@PathVariable Long productId,@RequestParam int decrease){
        return stockManagementService.decreaseStock(productId,decrease);
    }

    @PostMapping("/decrease/{productId}")
    public StockManagementDto increaseLevel(@PathVariable Long productId,@RequestParam int increase){
        return stockManagementService.increaseStock(productId,increase);
    }

    @PostMapping("/set-max/{productId}")
    public StockManagementDto setMaxQuantity(@PathVariable Long productId, @RequestParam int maxQuantity){
        return stockManagementService.setMaxQuantity(productId, maxQuantity);
    }

    @PostMapping("/reorder/{productId}")
    public  StockManagementDto setReorderLevel(@PathVariable Long productId, @RequestParam int reorderLevel){
        return  stockManagementService.setReorderLevel(productId, reorderLevel);
    }

    @GetMapping("/{productId}")
    public StockManagementDto getProduct(@PathVariable Long productId){
        return stockManagementService.getStockByProductId(productId);
    }

    @GetMapping("/checkStock/{productId}")
    public boolean checkStockAvailability(@PathVariable Long productId, @RequestParam int quantity){
        return stockManagementService.checkStockAvailability(productId,quantity);
    }
}
