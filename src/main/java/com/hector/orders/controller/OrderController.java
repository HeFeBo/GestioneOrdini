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
import com.hector.orders.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(@Qualifier("OrderService") OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> showOrders() {
        return ResponseEntity.ok(orderService.showOrders());
    }

    @PostMapping
    public ResponseEntity<OrderResponse> addOrder(@Valid @RequestBody OrderRequest dto) {
        return ResponseEntity.status(201).body(orderService.addOrder(dto));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> searchOrder(@PathVariable("orderId") long orderId) {
        return ResponseEntity.ok(orderService.searchOrder(orderId));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable("orderId") long orderId, @Valid @RequestBody OrderRequest dto) {
        return ResponseEntity.ok(orderService.updateOrder(orderId, dto));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("orderId") long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cliente/{customerId}")    
    public ResponseEntity<List<OrderResponse>> searchOrdersByCustomer(@PathVariable("customerId") long customerId) {
        return ResponseEntity.ok(orderService.searchOrdersByCustomer(customerId));
    }

    @GetMapping("/producto/{productId}") 
    public ResponseEntity<List<OrderResponse>> searchOrdersByProduct(@PathVariable("productId") long productId) {
        return ResponseEntity.ok(orderService.searchOrdersByProduct(productId));
    }

}
