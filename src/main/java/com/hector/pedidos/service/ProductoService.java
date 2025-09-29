package com.hector.pedidos.service;

import java.util.List;
import java.util.Set;

import com.hector.pedidos.dto.request.ProductoRequest;
import com.hector.pedidos.dto.response.ProductoResponse;

public interface ProductoService {
    List<ProductoResponse> listarProductos();
    ProductoResponse registrarProducto(ProductoRequest dto);
    ProductoResponse buscarProducto(long idProducto);
    void eliminarProducto(Long idProducto);
    List<ProductoResponse> productosPedido(Long idPedido);
    Set<ProductoResponse> productosCliente(Long idCliente);
}
