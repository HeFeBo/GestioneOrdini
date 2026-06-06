package com.hector.orders.service.interf;

import java.util.List;
import java.util.Set;

import com.hector.orders.dto.response.CustomerResponse;
import com.hector.orders.dto.response.OrderResponse;
import com.hector.orders.dto.response.ProductResponse;

public interface AuxiliaryService {
    List<ProductResponse> searchProductsByOrder(long orderId);
    Set<ProductResponse> searchProductsByCustomer(long customerId);
    CustomerResponse searchCustomerByOrder(long orderId);
    Set<CustomerResponse> searchCustomersByProduct(long productId);
    List<OrderResponse> searchOrdersByCustomer(long customerId);
    List<OrderResponse> searchOrdersByProduct(long productId);
    
}
