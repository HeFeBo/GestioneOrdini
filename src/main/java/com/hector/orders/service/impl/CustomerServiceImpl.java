package com.hector.orders.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hector.orders.dto.request.CustomerRequest;
import com.hector.orders.dto.response.CustomerResponse;
import com.hector.orders.exception.CustomerNotFoundException;
import com.hector.orders.exception.OrderNotFoundException;
import com.hector.orders.exception.ProductNotFoundException;
import com.hector.orders.mapper.CustomerMapper;
import com.hector.orders.model.Customer;
import com.hector.orders.model.Order;
import com.hector.orders.repository.CustomerRepo;
import com.hector.orders.repository.OrderRegistrationRepo;
import com.hector.orders.repository.OrderRepo;
import com.hector.orders.repository.ProductRepo;
import com.hector.orders.service.CustomerService;

@Qualifier("CustomerService")
@Service
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepo customerRepo;
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final OrderRegistrationRepo orderRegistrationRepo;

    public CustomerServiceImpl(CustomerRepo customerRepo, ProductRepo productRepo,
            OrderRepo orderRepo, OrderRegistrationRepo orderRegistrationRepo) {
        this.customerRepo = customerRepo;
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.orderRegistrationRepo = orderRegistrationRepo;
    }

    @Override
    public List<CustomerResponse> showCustomers() {
        List<Customer> customers = customerRepo.findAll();
        return customers.stream().map(c -> {
            CustomerResponse response = CustomerMapper.toDTO(c);
            return response;
        }).collect(Collectors.toList());

    }

    @Override
    public CustomerResponse addCustomer(CustomerRequest dto) {
        Customer customer = CustomerMapper.toEntity(dto);
    
        return CustomerMapper.toDTO(customerRepo.save(customer));
    }

    @Override
    public CustomerResponse searchCustomer(long customerId) {
        Customer customer = customerRepo.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + customerId + " non trovato."));
        
        return CustomerMapper.toDTO(customer);
    }

    @Override
    public CustomerResponse updateCustomer(long customerId, CustomerRequest dto) {
        Customer customer = customerRepo.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + customerId + " non trovato."));
        customer.setName(dto.getName());
        customer.setIdCard(dto.getIdCard());
        return CustomerMapper.toDTO(customerRepo.save(customer));
    }

    @Override
    public void deleteCustomer(long customerId) {
        customerRepo.deleteById(customerId);
    }

    @Override
    public CustomerResponse searchCustomerByOrder(long orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Ordine con ID " + orderId + " non trovato."));
        Customer customer = order.getCustomer();

        return CustomerMapper.toDTO(customer);
    }

    @Override
    public Set<CustomerResponse> searchCustomersByProduct(long productId) {
        productRepo.findById(productId).orElseThrow(() -> new ProductNotFoundException("Prodotto con ID " + productId + " non trovato."));
    
        return orderRegistrationRepo.findByProductId(productId).stream().map(r -> CustomerMapper.toDTO(r.getOrder().getCustomer())).collect(Collectors.toSet());
    }

}
