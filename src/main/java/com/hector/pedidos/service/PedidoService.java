package com.hector.pedidos.service;

import java.util.List;
import java.util.Set;

import com.hector.pedidos.dto.request.PedidoRequest;
import com.hector.pedidos.dto.response.PedidoResponse;

public interface PedidoService {
    List<PedidoResponse> listarPedidos();
    PedidoResponse agregarPedido(PedidoRequest dto);
    PedidoResponse buscarPedido(Long idPedido);
    void eliminarPedido(Long idPedido);
    List<PedidoResponse> pedidosCliente(Long idCliente);
    Set<PedidoResponse> pedidosProducto(Long idProducto);
}
