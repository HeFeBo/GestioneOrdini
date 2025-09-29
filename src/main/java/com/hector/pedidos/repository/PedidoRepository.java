package com.hector.pedidos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hector.pedidos.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long>{

}
