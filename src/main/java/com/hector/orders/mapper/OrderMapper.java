package com.hector.orders.mapper;

import org.springframework.stereotype.Component;

import com.hector.orders.dto.request.OrderRequest;
import com.hector.orders.dto.response.OrderResponse;
import com.hector.orders.model.Order;

@Component
public class OrderMapper {
    public OrderResponse toDTO(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setCustomerId(order.getCustomer().getId());
        response.setDeliveryDate(order.getDeliveryDate());
        response.setIssueDate(order.getIssueDate());
        response.setItemsId(order.getItems().stream().map(it -> it.getId()).toList());

        return response;
    }

    public Order toEntity(OrderRequest request){
        Order order = new Order();
        order.setIssueDate(request.getIssueDate());

        return order;
    }

}
