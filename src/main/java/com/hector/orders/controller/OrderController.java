package com.hector.orders.controller;

import java.util.List;

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

import com.hector.orders.dto.request.OrderRequest;
import com.hector.orders.dto.response.OrderResponse;
import com.hector.orders.service.interf.AuxiliaryService;
import com.hector.orders.service.interf.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final AuxiliaryService auxiliaryService;

    public OrderController(@Qualifier("OrderServiceImpl") OrderService orderService, @Qualifier("AuxiliaryServiceImpl") AuxiliaryService auxiliaryService) {
        this.orderService = orderService;
        this.auxiliaryService = auxiliaryService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> showOrders() {
        return ResponseEntity.ok(orderService.showOrders());
    }

    @PostMapping
    public ResponseEntity<OrderResponse> addOrder(@Valid @RequestBody OrderRequest dto) { //Lo hace el usuario
        return ResponseEntity.status(201).body(orderService.addOrder(dto));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> searchOrder(@PathVariable("orderId") long orderId) { //Lo hace el usuario y el admin
        return ResponseEntity.ok(orderService.searchOrder(orderId));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable("orderId") long orderId, @Valid @RequestBody OrderRequest dto) { //Lo hace el usuario
        return ResponseEntity.ok(orderService.updateOrder(orderId, dto));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("orderId") long orderId) { //Lo hace el usuario
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cliente/{customerId}")    
    public ResponseEntity<List<OrderResponse>> searchOrdersByCustomer(@PathVariable("customerId") long customerId) {
        return ResponseEntity.ok(auxiliaryService.searchOrdersByCustomer(customerId));
    }

    @GetMapping("/producto/{productId}") 
    public ResponseEntity<List<OrderResponse>> searchOrdersByProduct(@PathVariable("productId") long productId) {
        return ResponseEntity.ok(auxiliaryService.searchOrdersByProduct(productId));
    }

}
