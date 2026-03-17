package com.hector.orders.mapper;

import org.springframework.lang.NonNull;

import com.hector.orders.dto.response.OrderResponse;
import com.hector.orders.model.Customer;
import com.hector.orders.model.Order;

public class OrderMapper {
    public static @NonNull OrderResponse toDTO(Order order) {
        OrderResponse response = new OrderResponse(
                order.getId(),
                order.getCustomer().getId(),
                order.getRegistrations().stream().map(r -> r.getProduct().getId()).toList()
        );
        return response;
    }

    public static @NonNull Order toEntity(Customer customer){
        Order order = new Order(customer);
        return order;
    }

}
