package com.hector.orders.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hector.orders.dto.request.OrderRequest;
import com.hector.orders.dto.response.OrderResponse;
import com.hector.orders.exception.CustomerNotFoundException;
import com.hector.orders.exception.OrderNotFoundException;
import com.hector.orders.mapper.OrderMapper;
import com.hector.orders.model.Customer;
import com.hector.orders.model.Order;
import com.hector.orders.repository.CustomerRepo;
import com.hector.orders.repository.OrderRepo;
import com.hector.orders.service.interf.OrderService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Qualifier("OrderServiceImpl")
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final CustomerRepo customerRepo;
    private final OrderRepo orderRepo;

    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public List<OrderResponse> showOrders() {  
        List<Order> orders = orderRepo.findAll();
        return orders.stream().map(orderMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse addOrder(OrderRequest dto) {
        long customerId = dto.getCustomerId();
        Customer customer = customerRepo.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + customerId + " non trovato."));
        Order order = orderMapper.toEntity(dto);
        order.setCustomer(customer);
        Order orderSaved = orderRepo.save(order);

        return orderMapper.toDTO(orderSaved);
        
    }

    @Override
    @Transactional
    public OrderResponse searchOrder(long orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Ordine con ID " + orderId + " non trovato."));
        
        return orderMapper.toDTO(order);
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(long orderId, OrderRequest dto) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Ordine con ID " + orderId + " non trovato."));
        long customerId = dto.getCustomerId();
        Customer customer = customerRepo.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + customerId + " non trovato."));
        order.setCustomer(customer);
        order.setIssueDate(dto.getIssueDate());

        return orderMapper.toDTO(orderRepo.save(order));
    }


    @Override
    public void deleteOrder(long orderId) {
        if(!orderRepo.existsById(orderId)){
            throw new OrderNotFoundException("Ordine con ID " + orderId + " non trovato.");
        }

        orderRepo.deleteById(orderId);
    }

}
