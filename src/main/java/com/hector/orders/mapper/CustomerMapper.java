package com.hector.orders.mapper;

import org.springframework.stereotype.Component;

import com.hector.orders.dto.request.CustomerRequest;
import com.hector.orders.dto.response.CustomerResponse;
import com.hector.orders.model.Customer;

@Component
public class CustomerMapper {
    public CustomerResponse toDTO(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setSurname(customer.getSurname());
        response.setIdCard(customer.getIdCard());
        response.setCellPhone(customer.getCellPhone());
        response.setAddress(customer.getAddress());
        response.setIdOrders(customer.getOrders().stream()
            .map(or -> or.getId()).toList());

        return response;
    }

    public Customer toEntity(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setSurname(request.getSurname());
        customer.setIdCard(request.getIdCard());
        customer.setCellPhone(request.getCellPhone());
        customer.setAddress(request.getAddress());
        
        return customer;
    }

}
