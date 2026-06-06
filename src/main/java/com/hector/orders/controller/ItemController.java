package com.hector.orders.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hector.orders.dto.request.ItemRequest;
import com.hector.orders.dto.response.ItemResponse;
import com.hector.orders.dto.response.ProductResponse;
import com.hector.orders.service.interf.FinanceService;
import com.hector.orders.service.interf.ItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    private final ItemService itemService;
    private final FinanceService financeService;

    public ItemController(@Qualifier("ItemServiceImpl") ItemService itemService, FinanceService financeService) {
        this.itemService = itemService;
        this.financeService = financeService;
        
    }

    @GetMapping()
    public ResponseEntity<List<ItemResponse>> showItems(){
        return ResponseEntity.ok(itemService.showItems());
    }

    @PostMapping
    public ResponseEntity<ItemResponse> addItem(@Valid @RequestBody ItemRequest dto){
        return ResponseEntity.status(201).body(itemService.addItem(dto));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponse> searchItem(@PathVariable("itemId") long itemId){
        return ResponseEntity.ok(itemService.searchItem(itemId));
    } 

    @PutMapping("/{itemId}")
    public ResponseEntity<ItemResponse> updateItem(@PathVariable("itemId") long itemId, @Valid @RequestBody ItemRequest dto){
        return ResponseEntity.ok(itemService.updateItem(itemId, dto));
    }
    
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable("itemId") long itemId){
        itemService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/products-sold")
    public ResponseEntity<Set<ProductResponse>> showProductsSold(){
        return ResponseEntity.ok(financeService.showProductsSold());
    }

    @GetMapping("/products-sold/top/{positions}")
    public ResponseEntity<List<ProductResponse>> bestSellingProducts(@PathVariable("positions") int positions){
        return ResponseEntity.ok(financeService.bestSellingProducts(positions));
    }

    @GetMapping("/total-sale")
    public ResponseEntity<Double> totalSale(){
        return ResponseEntity.ok(financeService.totalSale());
    }

    @GetMapping("/total-sale/order/{orderId}")
    public ResponseEntity<Double> totalSaleByOrder(@PathVariable("orderId") long orderId){
        return ResponseEntity.ok(financeService.totalSaleByOrder(orderId));
    }

    @GetMapping("/total-sale/customer/{customerId}")
    public ResponseEntity<Double> totalSaleByCustomer(@PathVariable("customerId") long customerId){
        return ResponseEntity.ok(financeService.totalSaleByCustomer(customerId));
    }

    @GetMapping("/total-sale/product/{productId}")
    public ResponseEntity<Double> totalSaleByProduct(@PathVariable("productId") long productId){
        return ResponseEntity.ok(financeService.totalSaleByProduct(productId));
    }

    @GetMapping("/total-sale/average")
    public ResponseEntity<Double> averageTotalSale(){
        return ResponseEntity.ok(financeService.averageTotalSale());
    }

    @GetMapping("/total-units-sold")
    public ResponseEntity<Long> totalUnitsSold(){
        return ResponseEntity.ok(financeService.totalUnitsSold());
    }

    @GetMapping("/total-units-sold/order/{orderId}")
    public ResponseEntity<Integer> totalUnitsSoldByOrder(@PathVariable("orderId") long orderId){
        return ResponseEntity.ok(financeService.totalUnitsSoldByOrder(orderId));
    }

    @GetMapping("/total-units-sold/customer/{customerId}")
    public ResponseEntity<Integer> totalUnitsSoldByCustomer(@PathVariable("customerId") long customerId){
        return ResponseEntity.ok(financeService.totalUnitsSoldByCustomer(customerId));
    }

    @GetMapping("/total-units-sold/product/{productId}")
    public ResponseEntity<Integer> totalUnitsSoldByProduct(@PathVariable("productId") long productId){
        return ResponseEntity.ok(financeService.totalUnitsSoldByProduct(productId));
    }

}
