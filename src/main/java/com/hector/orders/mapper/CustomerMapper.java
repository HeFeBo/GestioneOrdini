package com.hector.orders.mapper;

import org.springframework.lang.NonNull;

import com.hector.orders.dto.request.CustomerRequest;
import com.hector.orders.dto.response.CustomerResponse;
import com.hector.orders.model.Customer;

public class CustomerMapper {
    public static @NonNull CustomerResponse toDTO(Customer customer) {
        CustomerResponse response = new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getIdCard(),
                customer.getOrders().stream().map(o -> o.getId()).toList()
        );
        return response;
    }

    public static @NonNull Customer toEntity(CustomerRequest request) {
        Customer customer = new Customer(request.getName(), request.getIdCard());
        return customer;
    }

}
