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

import com.hector.orders.dto.request.ProductRequest;
import com.hector.orders.dto.response.ProductResponse;
import com.hector.orders.service.interf.AuxiliaryService;
import com.hector.orders.service.interf.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final AuxiliaryService auxiliaryService;

    public ProductController(@Qualifier("ProductServiceImpl") ProductService productService, @Qualifier("AuxiliaryServiceImpl") AuxiliaryService auxiliaryService) {
        this.productService = productService;
        this.auxiliaryService = auxiliaryService;
    }

    //Estos metodos son para gestionar un inventario

    @GetMapping
    public ResponseEntity<List<ProductResponse>> showProducts() { //Lo hace el usuario y el admin
        return ResponseEntity.ok(productService.showProducts());
    }

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody ProductRequest dto) { 
        return ResponseEntity.status(201).body(productService.addProduct(dto));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> searchProduct(@PathVariable("productId") long productId) { //Lo hace el usuario y el admin
        return ResponseEntity.ok(productService.searchProduct(productId));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable("productId") long productId, @Valid @RequestBody ProductRequest dto) {
        return ResponseEntity.ok(productService.updateProduct(productId, dto));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    //Estos metodos son para gestionar los productos ya solicitados por los clientes

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<ProductResponse>> searchProductsByOrder(@PathVariable("orderId") long orderId) { //Lo hace el usuario y el admin
        return ResponseEntity.ok(auxiliaryService.searchProductsByOrder(orderId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Set<ProductResponse>> searchProductsByCustomer(@PathVariable("customerId") long customerId) {
        return ResponseEntity.ok(auxiliaryService.searchProductsByCustomer(customerId));
    }

}
