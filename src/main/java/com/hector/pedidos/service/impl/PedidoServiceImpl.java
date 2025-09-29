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
import com.hector.pedidos.dto.response.ProductoResponse;
import com.hector.pedidos.model.Cliente;
import com.hector.pedidos.model.Pedido;
import com.hector.pedidos.model.Producto;
import com.hector.pedidos.repository.ClienteRepository;
import com.hector.pedidos.repository.PedidoRepository;
import com.hector.pedidos.repository.ProductoRepository;
import com.hector.pedidos.service.PedidoService;
import com.hector.pedidos.service.ProductoService;

@Qualifier("ServicioPedido")
@Service
public class PedidoServiceImpl implements PedidoService{
    private final ClienteRepository repoCliente;
    private final ProductoRepository repoProducto;
    private final PedidoRepository repoPedido;
    private final ProductoService servicioProducto;

    public PedidoServiceImpl(ClienteRepository repoCliente, ProductoRepository repoProducto,
            PedidoRepository repoPedido, ProductoService servicioProducto) {
        this.repoCliente = repoCliente;
        this.repoProducto = repoProducto;
        this.repoPedido = repoPedido;
        this.servicioProducto = servicioProducto;
    }

    @Override
    public PedidoResponse agregarPedido(PedidoRequest dto) {
        Cliente cliente = repoCliente.findById(dto.getIdCliente()).orElseThrow(() -> new RuntimeException("No se encontro el cliente."));
        List<Producto> productos = new ArrayList<>();

        for(Long id : dto.getIdProductos()){
            Producto producto = repoProducto.findById(id).orElseThrow(() -> new RuntimeException("No se encontro el producto."));
            productos.add(producto);
        }
        Pedido pedido = new Pedido(cliente, productos);
        Pedido guardado = repoPedido.save(pedido);
        
        ClienteResponse respuestaCliente = new ClienteResponse(cliente.getId(), cliente.getNombre(), cliente.getDni());
        List<ProductoResponse> listaProductos = servicioProducto.productosPedido(guardado.getId());
    
        return new PedidoResponse(guardado.getId(), respuestaCliente, listaProductos);
        
    }

    @Override
    public PedidoResponse buscarPedido(Long idPedido) {
        Pedido pedido = repoPedido.findById(idPedido).orElseThrow(() -> new RuntimeException("No se encontro el pedido."));
        ClienteResponse respuestaCliente = new ClienteResponse(pedido.getCliente().getId(), pedido.getCliente().getNombre(), pedido.getCliente().getDni());
        List<ProductoResponse> productos = servicioProducto.productosPedido(idPedido); 

        return new PedidoResponse(idPedido, respuestaCliente, productos);
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
            List<ProductoResponse> productos = servicioProducto.productosPedido(p.getId());
            PedidoResponse respuestaPedido = new PedidoResponse(p.getId(), respuestaCliente, productos);
            pedidos.add(respuestaPedido);
        }
        return pedidos;
    }

    @Override
    public List<PedidoResponse> pedidosCliente(Long idCliente) {
        Cliente cliente = repoCliente.findById(idCliente).orElseThrow(() -> new RuntimeException("No se encontro el cliente."));
        ClienteResponse respuestaCliente = new ClienteResponse(idCliente, cliente.getNombre(), cliente.getDni());
        List<Pedido> pedidos = cliente.getPedidos();
        List<PedidoResponse> respuesta = new ArrayList<>();

        for(Pedido p : pedidos){
            List<ProductoResponse> productos = servicioProducto.productosPedido(p.getId());
            PedidoResponse respuestaPedido = new PedidoResponse(p.getId(), respuestaCliente, productos);
            respuesta.add(respuestaPedido);
        }
        return respuesta;
    }

    @Override
    public Set<PedidoResponse> pedidosProducto(Long idProducto) {
        List<Pedido> pedidos = repoPedido.findAll();
        Set<PedidoResponse> respuesta = new HashSet<>();

        for(Pedido p : pedidos){
            List<ProductoResponse> productos = servicioProducto.productosPedido(p.getId());
            for(ProductoResponse c : productos){
                if(c.getId().equals(idProducto)){
                    ClienteResponse respuestaCliente = new ClienteResponse(p.getCliente().getId(), p.getCliente().getNombre(), p.getCliente().getDni());
                    PedidoResponse respuestaPedido = new PedidoResponse(p.getId(), respuestaCliente, productos);
                    respuesta.add(respuestaPedido);
                }
            }
            
        }
        return respuesta;
    }

}
