package com.hector.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hector.orders.model.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Long>{

}
