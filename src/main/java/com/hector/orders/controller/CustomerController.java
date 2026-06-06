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

import com.hector.orders.dto.request.CustomerRequest;
import com.hector.orders.dto.response.CustomerResponse;
import com.hector.orders.service.interf.AuxiliaryService;
import com.hector.orders.service.interf.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final AuxiliaryService auxiliaryService;
    
    public CustomerController(
        @Qualifier("CustomerServiceImpl") CustomerService customerService, 
        @Qualifier("AuxiliaryServiceImpl") AuxiliaryService auxiliaryService) {
            this.customerService = customerService;
            this.auxiliaryService = auxiliaryService;
    }

    //Estos metodos son para que el usuario gestione su cuenta

    @PostMapping
    public ResponseEntity<CustomerResponse> addCustomer(@Valid @RequestBody CustomerRequest dto){ //Lo hace el usuario
        return ResponseEntity.status(201).body(customerService.addCustomer(dto));
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable("customerId") long customerId, @Valid @RequestBody CustomerRequest dto){ //Lo hace el usuario
        return ResponseEntity.ok(customerService.updateCustomer(customerId, dto));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("customerId") long customerId){ //Lo hace el usuario
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
    
    //Estos metodos son para gestionar a los usuarios

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> showCustomers(){ //
        return ResponseEntity.ok(customerService.showCustomers());
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> searchCustomer(@PathVariable("customerId") long customerId){
        return ResponseEntity.ok(customerService.searchCustomer(customerId));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<CustomerResponse> searchCustomerByOrder(@PathVariable("orderId") long orderId){
        return ResponseEntity.ok(auxiliaryService.searchCustomerByOrder(orderId));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Set<CustomerResponse>> searchCustomersByProduct(@PathVariable("productId") long productId){
        return ResponseEntity.ok(auxiliaryService.searchCustomersByProduct(productId));
    } 

}
