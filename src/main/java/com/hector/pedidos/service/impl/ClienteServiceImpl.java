package com.hector.pedidos.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hector.pedidos.dto.request.ClienteRequest;
import com.hector.pedidos.dto.response.ClienteResponse;
import com.hector.pedidos.exception.ClienteNoEncontradoException;
import com.hector.pedidos.model.Cliente;
import com.hector.pedidos.model.Pedido;
import com.hector.pedidos.model.Producto;
import com.hector.pedidos.repository.ClienteRepository;
import com.hector.pedidos.repository.PedidoRepository;
import com.hector.pedidos.repository.ProductoRepository;
import com.hector.pedidos.service.ClienteService;

@Qualifier("ServicioCliente")
@Service
public class ClienteServiceImpl implements ClienteService{
    private final ClienteRepository repoCliente;
    private final ProductoRepository repoProducto;
    private final PedidoRepository repoPedido;

    public ClienteServiceImpl(ClienteRepository repoCliente, ProductoRepository repoProducto,
            PedidoRepository repoPedido) {
        this.repoCliente = repoCliente;
        this.repoProducto = repoProducto;
        this.repoPedido = repoPedido;
    }

    @Override
    public List<ClienteResponse> listarClientes() {
        List<Cliente> clientes = repoCliente.findAll();
        List<ClienteResponse> respuesta = new ArrayList<>();
        for(Cliente c: clientes){
            respuesta.add(new ClienteResponse(c.getId(),c.getNombre(), c.getDni()));
        }
        return respuesta;
    }

    @Override
    public ClienteResponse buscarCliente(Long idCliente) {
        Cliente cliente = repoCliente.findById(idCliente).orElseThrow(() -> new ClienteNoEncontradoException("No se encontro el cliente con Id = " + idCliente));
        return new ClienteResponse(cliente.getId(), cliente.getNombre(), cliente.getDni());
    }

    @Override
    public ClienteResponse buscarClientePedido(Long idPedido) {
        Pedido pedido = repoPedido.findById(idPedido).orElseThrow(() -> new RuntimeException("No se encontro el pedido con Id = " + idPedido));
        Cliente cliente = pedido.getCliente();
        return new ClienteResponse(cliente.getId(), cliente.getNombre(), cliente.getDni());
    }

    @Override
    public Set<ClienteResponse> buscarClientesProducto(Long idProducto) {
        Producto producto = repoProducto.findById(idProducto).orElseThrow(() -> new RuntimeException("No se encontro el producto con Id = " + idProducto));
        Set<ClienteResponse> respuesta = new HashSet<>();
        List<Pedido> pedidos = repoPedido.findAll();
        for(Pedido p : pedidos){
            for(Producto c : p.getProductos()){
                if(c.equals(producto)){
                    respuesta.add(new ClienteResponse(p.getCliente().getId(), p.getCliente().getNombre(), p.getCliente().getDni()));
                }
            }
            
        }
        
        return respuesta;
    }

    @Override
    public void eliminarCliente(Long idCliente) {
        repoCliente.deleteById(idCliente);
    }

    @Override
    public ClienteResponse registrarCliente(ClienteRequest dto) {
        Cliente cliente = new Cliente(dto.getNombre(), dto.getDni());
        Cliente guardado = repoCliente.save(cliente);
        return new ClienteResponse(guardado.getId(), guardado.getNombre(), guardado.getDni());      
    }

}
