package com.hector.pedidos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hector.pedidos.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{

}
