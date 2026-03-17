package com.hector.orders.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hector.orders.dto.request.OrderRequest;
import com.hector.orders.dto.response.OrderResponse;
import com.hector.orders.exception.CustomerNotFoundException;
import com.hector.orders.exception.OrderNotFoundException;
import com.hector.orders.exception.ProductNotFoundException;
import com.hector.orders.mapper.OrderMapper;
import com.hector.orders.model.Customer;
import com.hector.orders.model.Order;
import com.hector.orders.repository.CustomerRepo;
import com.hector.orders.repository.OrderRegistrationRepo;
import com.hector.orders.repository.OrderRepo;
import com.hector.orders.repository.ProductRepo;
import com.hector.orders.service.OrderService;

@Qualifier("OrderService")
@Service
public class OrderServiceImpl implements OrderService{
    private final CustomerRepo customerRepo;
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final OrderRegistrationRepo orderRegistrationRepo;

    public OrderServiceImpl(CustomerRepo customerRepo, ProductRepo productRepo,
            OrderRepo orderRepo, OrderRegistrationRepo orderRegistrationRepo) {
        this.customerRepo = customerRepo;
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.orderRegistrationRepo = orderRegistrationRepo;
    }

    @Override
    public List<OrderResponse> showOrders() {  
        List<Order> orders = orderRepo.findAll();
        return orders.stream().map(o -> OrderMapper.toDTO(o)).collect(Collectors.toList());
    }

    @Override
    public OrderResponse addOrder(OrderRequest dto) {
        long customerId = dto.getCustomerId();
        Customer customer = customerRepo.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + customerId + " non trovato."));
        Order order = OrderMapper.toEntity(customer);
        Order orderSaved = orderRepo.save(order);

        return OrderMapper.toDTO(orderSaved);
        
    }

    @Override
    public OrderResponse searchOrder(long orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Ordine con ID " + orderId + " non trovato."));
        return OrderMapper.toDTO(order);
    }

    @Override
    public OrderResponse updateOrder(long orderId, OrderRequest dto) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Ordine con ID " + orderId + " non trovato."));
        long customerId = dto.getCustomerId();
        Customer customer = customerRepo.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + customerId + " non trovato."));
        order.setCustomer(customer);
        return OrderMapper.toDTO(orderRepo.save(order));
    }


    @Override
    public void deleteOrder(long orderId) {
        if(!orderRepo.existsById(orderId)){
            throw new OrderNotFoundException("Ordine con ID " + orderId + " non trovato.");
        }

        orderRepo.deleteById(orderId);
    }

    @Override
    public List<OrderResponse> searchOrdersByCustomer(long customerId) {
        Customer customer = customerRepo.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + customerId + " non trovato."));
        List<Order> orders = customer.getOrders();
        return orders.stream().map(o -> OrderMapper.toDTO(o)).collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> searchOrdersByProduct(long productId) { 
        productRepo.findById(productId).orElseThrow(() -> new ProductNotFoundException("Prodotto con ID " + productId + " non trovato."));

        return orderRegistrationRepo.findByProductId(productId).stream().map(r -> OrderMapper.toDTO(r.getOrder())).collect(Collectors.toList());

    }

}
