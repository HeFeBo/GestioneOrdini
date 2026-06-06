package com.hector.orders.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hector.orders.dto.response.QuantityProductsResponse;
import com.hector.orders.model.Item;

public interface ItemRepo extends JpaRepository<Item, Long>{
    List<Item> findByOrderId(Long orderId);
    List<Item> findByProductId(Long productId);

    @Query("SELECT new com.hector.orders.dto.response.QuantityProductsResponse(pp.product.id, SUM(pp.quantity)) "+
       "FROM Item pp "+
       "GROUP BY pp.product.id "+
       "ORDER BY SUM(pp.quantity) DESC")
    List<QuantityProductsResponse> findProductsOrderedByQuantity();
}
