package com.hector.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hector.orders.model.Product;

public interface ProductRepo extends JpaRepository<Product, Long>{

}
