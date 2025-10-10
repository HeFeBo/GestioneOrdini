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
import com.hector.pedidos.exception.PedidoNoEncontradoException;
import com.hector.pedidos.exception.ProductoNoEncontradoException;
import com.hector.pedidos.model.Cliente;
import com.hector.pedidos.model.Pedido;
import com.hector.pedidos.model.Producto;
import com.hector.pedidos.model.ProductoPedido;
import com.hector.pedidos.repository.ClienteRepository;
import com.hector.pedidos.repository.PedidoRepository;
import com.hector.pedidos.repository.ProductoPedidoRepository;
import com.hector.pedidos.repository.ProductoRepository;
import com.hector.pedidos.service.ClienteService;

@Qualifier("ServicioCliente")
@Service
public class ClienteServiceImpl implements ClienteService{
    private final ClienteRepository repoCliente;
    private final ProductoRepository repoProducto;
    private final PedidoRepository repoPedido;
    private final ProductoPedidoRepository repoProductoPedido;

    public ClienteServiceImpl(ClienteRepository repoCliente, ProductoRepository repoProducto,
            PedidoRepository repoPedido, ProductoPedidoRepository repoProductoPedido) {
        this.repoCliente = repoCliente;
        this.repoProducto = repoProducto;
        this.repoPedido = repoPedido;
        this.repoProductoPedido = repoProductoPedido;
    }

    @Override
    public List<ClienteResponse> listarClientes() {
        List<Cliente> clientes = repoCliente.findAll();
        List<ClienteResponse> respuesta = new ArrayList<>();
        for(Cliente c: clientes){
            List<Pedido> listaPedido = c.getPedidos();
            List<Long> idPedidos = new ArrayList<>();
            for(Pedido p : listaPedido){
                idPedidos.add(p.getId());
            }
            respuesta.add(new ClienteResponse(c.getId(),c.getNombre(), c.getDni(), idPedidos));
        }
        return respuesta;
    }

    @Override
    public ClienteResponse buscarCliente(Long idCliente) {
        Cliente cliente = repoCliente.findById(idCliente).orElseThrow(() -> new ClienteNoEncontradoException("No se encontro el cliente con Id = " + idCliente));
        List<Pedido> listaPedido = cliente.getPedidos();
        List<Long> idPedidos = new ArrayList<>();

        for(Pedido p : listaPedido){
            idPedidos.add(p.getId());
        }

        return new ClienteResponse(cliente.getId(), cliente.getNombre(), cliente.getDni(), idPedidos);
    }

    @Override
    public ClienteResponse buscarClientePedido(Long idPedido) {
        Pedido pedido = repoPedido.findById(idPedido).orElseThrow(() -> new PedidoNoEncontradoException("No se encontro el pedido con Id = " + idPedido));
        Cliente cliente = pedido.getCliente();

        List<Pedido> listaPedido = cliente.getPedidos();
        List<Long> idPedidos = new ArrayList<>();

        for(Pedido p : listaPedido){
            idPedidos.add(p.getId());
        }

        return new ClienteResponse(cliente.getId(), cliente.getNombre(), cliente.getDni(), idPedidos);
    }

    @Override
    public Set<ClienteResponse> buscarClientesProducto(Long idProducto) {
        Producto producto = repoProducto.findById(idProducto).orElseThrow(() -> new ProductoNoEncontradoException("No se encontro el producto con Id = " + idProducto));
        Set<ClienteResponse> respuesta = new HashSet<>();

        List<ProductoPedido> productosPedidos = repoProductoPedido.findAll();
        for(ProductoPedido pp : productosPedidos){
            if(pp.getProducto().equals(producto)){
                Cliente cliente = pp.getPedido().getCliente();
                List<Pedido> listaPedido = cliente.getPedidos();
                List<Long> idPedidos = new ArrayList<>();

                for(Pedido p : listaPedido){
                    idPedidos.add(p.getId());
                }

                ClienteResponse respuestaCliente = new ClienteResponse(cliente.getId(), cliente.getNombre(), cliente.getDni(), idPedidos);
                respuesta.add(respuestaCliente);
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
        Cliente clienteGuardado = repoCliente.save(cliente);

        List<Long> idPedidos = new ArrayList<>();

        return new ClienteResponse(clienteGuardado.getId(), clienteGuardado.getNombre(), clienteGuardado.getDni(), idPedidos);      
    }

}
