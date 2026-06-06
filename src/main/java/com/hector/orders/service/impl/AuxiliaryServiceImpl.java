package com.hector.orders.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hector.orders.dto.response.CustomerResponse;
import com.hector.orders.dto.response.OrderResponse;
import com.hector.orders.dto.response.ProductResponse;
import com.hector.orders.exception.CustomerNotFoundException;
import com.hector.orders.exception.OrderNotFoundException;
import com.hector.orders.exception.ProductNotFoundException;
import com.hector.orders.mapper.CustomerMapper;
import com.hector.orders.mapper.OrderMapper;
import com.hector.orders.mapper.ProductMapper;
import com.hector.orders.model.Customer;
import com.hector.orders.model.Item;
import com.hector.orders.model.Order;
import com.hector.orders.repository.CustomerRepo;
import com.hector.orders.repository.ItemRepo;
import com.hector.orders.repository.OrderRepo;
import com.hector.orders.repository.ProductRepo;
import com.hector.orders.service.interf.AuxiliaryService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Qualifier("AuxiliaryServiceImpl")
@Service
@RequiredArgsConstructor
public class AuxiliaryServiceImpl implements AuxiliaryService{
    private final ItemRepo itemRepo;
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final CustomerRepo customerRepo;

    private final ProductMapper productMapper;
    private final CustomerMapper customerMapper;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public List<ProductResponse> searchProductsByOrder(long orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow(
            () -> new OrderNotFoundException("Ordine con ID " + orderId + " non trovato."));
        List<Item> items = order.getItems();
        List<ProductResponse> response = items.stream()
            .map(it -> it.getProduct())
            .map(productMapper::toDTO)
            .collect(Collectors.toList());

        return response;
    }

    @Override
    @Transactional
    public Set<ProductResponse> searchProductsByCustomer(long customerId) {Customer customer = customerRepo.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + customerId + " non trovato."));
        List<Order> orders = customer.getOrders();
        List<Item> items = orders.stream().flatMap(or -> or.getItems().stream()).collect(Collectors.toList());
        Set<ProductResponse> response = items.stream().map(r -> r.getProduct()).map(productMapper::toDTO).collect(Collectors.toSet());
        return response;
    }

    @Override
    @Transactional
    public CustomerResponse searchCustomerByOrder(long orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Ordine con ID " + orderId + " non trovato."));
        Customer customer = order.getCustomer();

        return customerMapper.toDTO(customer);
    }

    @Override
    @Transactional
    public Set<CustomerResponse> searchCustomersByProduct(long productId) {
        if(!productRepo.existsById(productId)) throw new ProductNotFoundException("Prodotto con ID " + productId + " non trovato.");
        
        List<Item> items = itemRepo.findByProductId(productId);

        List<Order> orders = items.stream().map(it -> it.getOrder()).toList();

        List<Customer> customers = orders.stream().map(or -> or.getCustomer()).toList();

        return customers.stream()
            .map(cu -> customerMapper.toDTO(cu))
            .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public List<OrderResponse> searchOrdersByCustomer(long customerId) {
        Customer customer = customerRepo.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + customerId + " non trovato."));
        List<Order> orders = customer.getOrders();

        return orders.stream().map(orderMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<OrderResponse> searchOrdersByProduct(long productId) {
        productRepo.findById(productId).orElseThrow(() -> new ProductNotFoundException("Prodotto con ID " + productId + " non trovato."));
        List<Item> items = itemRepo.findByProductId(productId);
        List<Order> orders = items.stream().map(it -> it.getOrder()).toList();

        return orders.stream().map(or -> orderMapper.toDTO(or)).collect(Collectors.toList());
    }

}
