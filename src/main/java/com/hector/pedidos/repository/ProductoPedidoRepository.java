package com.hector.pedidos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hector.pedidos.dto.response.ProductoCantidadResponse;
import com.hector.pedidos.model.ProductoPedido;

public interface ProductoPedidoRepository extends JpaRepository<ProductoPedido, Long>{
    List<ProductoPedido> findByPedidoId(Long idPedido);
    List<ProductoPedido> findByProductoId(Long idProducto);

    @Query("SELECT new com.hector.pedidos.dto.response.ProductoCantidadResponse(pp.producto.id, SUM(pp.cantidad)) "+
           "FROM ProductoPedido pp "+
           "GROUP BY pp.producto.id "+
           "ORDER BY SUM(pp.cantidad) DESC")
    List<ProductoCantidadResponse> findProductosOrdenadosPorCantidad();
}
