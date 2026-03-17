package com.hector.orders.service;

import java.util.List;

import com.hector.orders.dto.request.OrderRequest;
import com.hector.orders.dto.response.OrderResponse;

public interface OrderService {
    List<OrderResponse> showOrders();
    OrderResponse addOrder(OrderRequest dto);
    OrderResponse searchOrder(long orderId);
    OrderResponse updateOrder(long orderId, OrderRequest dto);
    void deleteOrder(long orderId);
    List<OrderResponse> searchOrdersByCustomer(long customerId);
    List<OrderResponse> searchOrdersByProduct(long productId);
}
