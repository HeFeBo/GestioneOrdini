package com.hector.orders.mapper;

import org.springframework.lang.NonNull;

import com.hector.orders.dto.request.OrderRegistrationRequest;
import com.hector.orders.dto.response.OrderRegistrationResponse;
import com.hector.orders.model.Order;
import com.hector.orders.model.OrderRegistration;
import com.hector.orders.model.Product;

public class OrderRegistrationMapper {
    public static @NonNull OrderRegistrationResponse toDTO(OrderRegistration registration) {
        OrderRegistrationResponse response = new OrderRegistrationResponse(
                registration.getId(),
                registration.getOrder().getId(),
                registration.getProduct().getId(),
                registration.getQuantity()
        );
        return response;
    }

    public static @NonNull OrderRegistration toEntity(OrderRegistrationRequest dto, Order order, Product product) {
        OrderRegistration registration = new OrderRegistration(order, product, dto.getQuantity());
        return registration;
    }

}
