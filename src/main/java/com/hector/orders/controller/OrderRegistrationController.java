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

import com.hector.orders.dto.request.OrderRegistrationRequest;
import com.hector.orders.dto.response.OrderRegistrationResponse;
import com.hector.orders.dto.response.ProductResponse;
import com.hector.orders.service.OrderRegistrationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders-registration")
public class OrderRegistrationController {
    private final OrderRegistrationService orderRegistrationService;

    public OrderRegistrationController(@Qualifier("OrderRegistrationService") OrderRegistrationService orderRegistrationService) {
        this.orderRegistrationService = orderRegistrationService;
    }

    @GetMapping()
    public ResponseEntity<List<OrderRegistrationResponse>> showOrdersRegistration(){
        return ResponseEntity.ok(orderRegistrationService.showOrdersRegistration());
    }

    @PostMapping
    public ResponseEntity<OrderRegistrationResponse> addOrderRegistration(@Valid @RequestBody OrderRegistrationRequest dto){
        return ResponseEntity.status(201).body(orderRegistrationService.addOrderRegistration(dto));
    }

    @GetMapping("/{registrationId}")
    public ResponseEntity<OrderRegistrationResponse> searchOrderRegistration(@PathVariable("registrationId") long OrderRegistrationId){
        return ResponseEntity.ok(orderRegistrationService.searchOrderRegistration(OrderRegistrationId));
    } 

    @PutMapping("/{registrationId}")
    public ResponseEntity<OrderRegistrationResponse> updateOrderRegistration(@PathVariable("registrationId") long OrderRegistrationId, @Valid @RequestBody OrderRegistrationRequest dto){
        return ResponseEntity.ok(orderRegistrationService.updateOrderRegistration(OrderRegistrationId, dto));
    }
    
    @DeleteMapping("/{registrationId}")
    public ResponseEntity<Void> deleteOrderRegistration(@PathVariable("registrationId") long OrderRegistrationId){
        orderRegistrationService.deleteOrderRegistration(OrderRegistrationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/products-sold")
    public ResponseEntity<Set<ProductResponse>> showProductsSold(){
        return ResponseEntity.ok(orderRegistrationService.showProductsSold());
    }

    @GetMapping("/products-sold/{orderId}")
    public ResponseEntity<List<OrderRegistrationResponse>> showProductsSoldByOrder(@PathVariable("orderId") long orderId){
        return ResponseEntity.ok(orderRegistrationService.showProductsSoldByOrder(orderId));
    }
    
    @GetMapping("/products-sold/top/{positions}")
    public ResponseEntity<List<ProductResponse>> bestSellingProducts(@PathVariable("positions") int positions){
        return ResponseEntity.ok(orderRegistrationService.bestSellingProducts(positions));
    }

    @GetMapping("/total-sale")
    public ResponseEntity<Double> totalSale(){
        return ResponseEntity.ok(orderRegistrationService.totalSale());
    }

    @GetMapping("/total-sale/order/{orderId}")
    public ResponseEntity<Double> totalSaleByOrder(@PathVariable("orderId") long orderId){
        return ResponseEntity.ok(orderRegistrationService.totalSaleByOrder(orderId));
    }

    @GetMapping("/total-sale/customer/{customerId}")
    public ResponseEntity<Double> totalSaleByCustomer(@PathVariable("customerId") long customerId){
        return ResponseEntity.ok(orderRegistrationService.totalSaleByCustomer(customerId));
    }

    @GetMapping("/total-sale/product/{productId}")
    public ResponseEntity<Double> totalSaleByProduct(@PathVariable("productId") long productId){
        return ResponseEntity.ok(orderRegistrationService.totalSaleByProduct(productId));
    }

    @GetMapping("/total-sale/average")
    public ResponseEntity<Double> averageTotalSale(){
        return ResponseEntity.ok(orderRegistrationService.averageTotalSale());
    }

    @GetMapping("/total-units-sold")
    public ResponseEntity<Long> totalUnitsSold(){
        return ResponseEntity.ok(orderRegistrationService.totalUnitsSold());
    }

    @GetMapping("/total-units-sold/order/{orderId}")
    public ResponseEntity<Integer> totalUnitsSoldByOrder(@PathVariable("orderId") long orderId){
        return ResponseEntity.ok(orderRegistrationService.totalUnitsSoldByOrder(orderId));
    }

    @GetMapping("/total-units-sold/customer/{customerId}")
    public ResponseEntity<Integer> totalUnitsSoldByCustomer(@PathVariable("customerId") long customerId){
        return ResponseEntity.ok(orderRegistrationService.totalUnitsSoldByCustomer(customerId));
    }

    @GetMapping("/total-units-sold/product/{productId}")
    public ResponseEntity<Integer> totalUnitsSoldByProduct(@PathVariable("productId") long productId){
        return ResponseEntity.ok(orderRegistrationService.totalUnitsSoldByProduct(productId));
    }

}
