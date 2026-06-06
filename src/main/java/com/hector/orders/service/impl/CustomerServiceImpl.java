package com.hector.orders.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hector.orders.dto.request.CustomerRequest;
import com.hector.orders.dto.response.CustomerResponse;
import com.hector.orders.exception.CustomerNotFoundException;
import com.hector.orders.mapper.CustomerMapper;
import com.hector.orders.model.Customer;
import com.hector.orders.repository.CustomerRepo;
import com.hector.orders.service.interf.CustomerService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Qualifier("CustomerServiceImpl")
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepo customerRepo;

    private final CustomerMapper customerMapper;

    @Override
    @Transactional
    public List<CustomerResponse> showCustomers() {
        List<Customer> customers = customerRepo.findAll();

        return customers.stream().map(c -> {
            CustomerResponse response = customerMapper.toDTO(c);
            return response;
        }).collect(Collectors.toList());

    }

    @Override
    public CustomerResponse addCustomer(CustomerRequest dto) {
        Customer customer = customerMapper.toEntity(dto);

        if(customer == null) throw new RuntimeException(); // creare un'eccezione personalizzata.

        Customer savedCustomer = customerRepo.save(customer);
    
        return customerMapper.toDTO(savedCustomer);
    }

    @Override
    @Transactional
    public CustomerResponse searchCustomer(long customerId) {
        Customer customer = customerRepo.findById(customerId).orElseThrow(
            () -> new CustomerNotFoundException(
                "Cliente con ID " + customerId + " non trovato."));
        
        return customerMapper.toDTO(customer);
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(long customerId, CustomerRequest dto) {
        Customer customer = customerRepo.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + customerId + " non trovato."));
        customer.setName(dto.getName());
        customer.setSurname(dto.getSurname());
        customer.setIdCard(dto.getIdCard());
        customer.setCellPhone(dto.getCellPhone());
        customer.setAddress(dto.getAddress());

        Customer savedCustomer = customerRepo.save(customer);

        return customerMapper.toDTO(savedCustomer);
    }

    @Override
    public void deleteCustomer(long customerId) {
        customerRepo.deleteById(customerId);
    }

}
