package com.hector.orders.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hector.orders.model.Account;

public interface AccountRepo extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountName(String accountName);
}
