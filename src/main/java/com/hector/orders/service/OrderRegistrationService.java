package com.hector.orders.service;

import java.util.List;
import java.util.Set;

import com.hector.orders.dto.request.OrderRegistrationRequest;
import com.hector.orders.dto.response.OrderRegistrationResponse;
import com.hector.orders.dto.response.ProductResponse;

public interface OrderRegistrationService {
    List<OrderRegistrationResponse> showOrdersRegistration();
    OrderRegistrationResponse addOrderRegistration(OrderRegistrationRequest dto);
    OrderRegistrationResponse searchOrderRegistration(long OrderRegistrationId);
    OrderRegistrationResponse updateOrderRegistration(long OrderRegistrationId, OrderRegistrationRequest dto);
    void deleteOrderRegistration(long OrderRegistrationId);
    Set<ProductResponse> showProductsSold();
    List<OrderRegistrationResponse> showProductsSoldByOrder(long orderId); 
    List<ProductResponse> bestSellingProducts(int positions);
    double totalSale();
    double totalSaleByOrder(long orderId);
    double totalSaleByCustomer(long customerId);
    double totalSaleByProduct(long productId);
    double averageTotalSale();
    long totalUnitsSold();
    int totalUnitsSoldByOrder(long orderId);
    int totalUnitsSoldByCustomer(long customerId);
    int totalUnitsSoldByProduct(long productId);
}
