package com.hector.pedidos.service;

import java.util.List;
import java.util.Set;

import com.hector.pedidos.dto.request.ProductoPedidoRequest;
import com.hector.pedidos.dto.response.ProductoPedidoResponse;
import com.hector.pedidos.dto.response.ProductoResponse;

public interface ProductoPedidoService {
    List<ProductoPedidoResponse> listarProductoPedido();
    ProductoPedidoResponse agregarProductoPedido(ProductoPedidoRequest dto);
    Set<ProductoResponse> listarProductosVendidos();
    List<ProductoPedidoResponse> listarProductosVendidosPorPedido(Long idPedido); 
    List<ProductoResponse> productosMasVendidos(Integer cantProd);
    Double ventaTotalGenerada();
    Double ventaTotalPedido(Long idPedido);
    Double ventaTotalCliente(Long idCliente);
    Double ventaTotalProducto(Long idProducto);
    Double promedioVentaDePedidos();
    Long totalUnidadesVendidas();
    Integer totalUnidadesVendidasPorPedido(Long idPedido);
    Integer totalUnidadesVendidasPorCliente(Long idCliente);
    Integer totalUnidadesVendidasPorProducto(Long idProducto);
}
