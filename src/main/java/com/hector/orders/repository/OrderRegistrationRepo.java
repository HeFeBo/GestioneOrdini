package com.hector.orders.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hector.orders.dto.response.QuantityProductsResponse;
import com.hector.orders.model.OrderRegistration;

public interface OrderRegistrationRepo extends JpaRepository<OrderRegistration, Long>{
    List<OrderRegistration> findByOrderId(Long orderId);
    List<OrderRegistration> findByProductId(Long productId);

    @Query("SELECT new com.hector.orders.dto.response.QuantityProductsResponse(pp.product.id, SUM(pp.quantity)) "+
       "FROM OrderRegistration pp "+
       "GROUP BY pp.product.id "+
       "ORDER BY SUM(pp.quantity) DESC")
    List<QuantityProductsResponse> findProductsOrderedByQuantity();
}
