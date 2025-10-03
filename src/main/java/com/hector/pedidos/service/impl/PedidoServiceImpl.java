package com.hector.pedidos.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hector.pedidos.dto.request.PedidoRequest;
import com.hector.pedidos.dto.response.ClienteResponse;
import com.hector.pedidos.dto.response.PedidoResponse;
import com.hector.pedidos.exception.ClienteNoEncontradoException;
import com.hector.pedidos.exception.PedidoNoEncontradoException;
import com.hector.pedidos.exception.ProductoNoEncontradoException;
import com.hector.pedidos.model.Cliente;
import com.hector.pedidos.model.Pedido;
import com.hector.pedidos.model.ProductoPedido;
import com.hector.pedidos.repository.ClienteRepository;
import com.hector.pedidos.repository.PedidoRepository;
import com.hector.pedidos.repository.ProductoPedidoRepository;
import com.hector.pedidos.repository.ProductoRepository;
import com.hector.pedidos.service.PedidoService;

@Qualifier("ServicioPedido")
@Service
public class PedidoServiceImpl implements PedidoService{
    private final ClienteRepository repoCliente;
    private final ProductoRepository repoProducto;
    private final PedidoRepository repoPedido;
    private final ProductoPedidoRepository repoProductoPedido;

    public PedidoServiceImpl(ClienteRepository repoCliente, ProductoRepository repoProducto,
            PedidoRepository repoPedido, ProductoPedidoRepository repoProductoPedido) {
        this.repoCliente = repoCliente;
        this.repoProducto = repoProducto;
        this.repoPedido = repoPedido;
        this.repoProductoPedido = repoProductoPedido;
    }

    @Override
    public PedidoResponse agregarPedido(PedidoRequest dto) {
        Cliente cliente = repoCliente.findById(dto.getIdCliente()).orElseThrow(() -> new ClienteNoEncontradoException("El cliente con ID " + dto.getIdCliente() + " no existe."));
        
        Pedido pedido = new Pedido(cliente);
        Pedido guardado = repoPedido.save(pedido);
        
        ClienteResponse respuestaCliente = new ClienteResponse(cliente.getId(), cliente.getNombre(), cliente.getDni());
        
        return new PedidoResponse(guardado.getId(), respuestaCliente);
        
    }

    @Override
    public PedidoResponse buscarPedido(Long idPedido) {
        Pedido pedido = repoPedido.findById(idPedido).orElseThrow(() -> new PedidoNoEncontradoException("El pedido con ID " + idPedido + " no existe."));
        ClienteResponse respuestaCliente = new ClienteResponse(pedido.getCliente().getId(), pedido.getCliente().getNombre(), pedido.getCliente().getDni());

        return new PedidoResponse(idPedido, respuestaCliente);
    }

    @Override
    public void eliminarPedido(Long idPedido) {
        repoPedido.deleteById(idPedido);
    }

    @Override
    public List<PedidoResponse> listarPedidos() {  
        List<Pedido> guardados = repoPedido.findAll();
        List<PedidoResponse> pedidos = new ArrayList<>();

        for(Pedido p : guardados){
            ClienteResponse respuestaCliente = new ClienteResponse(p.getCliente().getId(), p.getCliente().getNombre(), p.getCliente().getDni());
            PedidoResponse respuestaPedido = new PedidoResponse(p.getId(), respuestaCliente);
            pedidos.add(respuestaPedido);
        }
        return pedidos;
    }

    @Override
    public List<PedidoResponse> pedidosCliente(Long idCliente) {
        Cliente cliente = repoCliente.findById(idCliente).orElseThrow(() -> new ClienteNoEncontradoException("El cliente con ID " + idCliente + " no existe."));
        ClienteResponse respuestaCliente = new ClienteResponse(idCliente, cliente.getNombre(), cliente.getDni());
        List<Pedido> pedidos = cliente.getPedidos();
        List<PedidoResponse> respuesta = new ArrayList<>();

        for(Pedido p : pedidos){
            PedidoResponse respuestaPedido = new PedidoResponse(p.getId(), respuestaCliente);
            respuesta.add(respuestaPedido);
        }

        return respuesta;
    }

    @Override
    public Set<PedidoResponse> pedidosProducto(Long idProducto) {
        if(!repoProducto.existsById(idProducto)){
            throw new ProductoNoEncontradoException("El producto con ID " + idProducto + " no existe");
        }

        List<ProductoPedido> listaProductoPedido = repoProductoPedido.findByProductoId(idProducto);

        Set<PedidoResponse> respuesta = new HashSet<>();

        for(ProductoPedido pp : listaProductoPedido){
            Pedido pedido = pp.getPedido();
            Cliente cliente = pedido.getCliente();
            ClienteResponse clienteResponse = new ClienteResponse(cliente.getId(), cliente.getNombre(), cliente.getDni());
            PedidoResponse pedidoResponse = new PedidoResponse(pedido.getId(), clienteResponse);
            
            respuesta.add(pedidoResponse);
        }
        return respuesta;
    }

}
