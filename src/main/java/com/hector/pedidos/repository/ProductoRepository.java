package com.hector.pedidos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hector.pedidos.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long>{

}
