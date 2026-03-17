package com.hector.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hector.orders.model.Order;

public interface OrderRepo extends JpaRepository<Order, Long>{

}
