package com.hector.pedidos.service;

import java.util.List;
import java.util.Set;

import com.hector.pedidos.dto.request.ClienteRequest;
import com.hector.pedidos.dto.response.ClienteResponse;

public interface ClienteService {
    List<ClienteResponse> listarClientes();
    ClienteResponse registrarCliente(ClienteRequest dto);
    ClienteResponse buscarCliente(Long idCliente);
    void eliminarCliente(Long idCliente);
    ClienteResponse buscarClientePedido(Long idPedido);
    Set<ClienteResponse> buscarClientesProducto(Long idProducto);
}
