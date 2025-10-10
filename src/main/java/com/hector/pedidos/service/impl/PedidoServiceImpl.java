package com.hector.pedidos.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hector.pedidos.dto.request.PedidoRequest;
import com.hector.pedidos.dto.request.ProductoPedidoRequest;
import com.hector.pedidos.dto.response.ClienteResponse;
import com.hector.pedidos.dto.response.PedidoResponse;
import com.hector.pedidos.dto.response.ProductoPedidoResponse;
import com.hector.pedidos.dto.response.ProductoResponse;
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
        List<ProductoPedidoRequest> listaProductoPedidoRequest = dto.getListaProductoPedido();
        List<ProductoPedido> listaProductoPedido = new ArrayList<>();
        List<ProductoPedidoResponse> listaProductoPedidoResponse = new ArrayList<>();
        
        Pedido pedido = new Pedido(cliente);
        Pedido pedidoGuardado = repoPedido.save(pedido);

        for(ProductoPedidoRequest p : listaProductoPedidoRequest){
            Producto producto = repoProducto.findById(p.getIdProducto()).orElseThrow(() -> new ProductoNoEncontradoException("El cliente con ID " + p.getIdProducto() + " no existe."));
            ProductoResponse productoResponse = new ProductoResponse(producto.getId(), producto.getNombre(), producto.getOrigen(), producto.getPrecio());
            ProductoPedido productoPedido = new ProductoPedido(pedidoGuardado, producto, p.getCantidad());
            ProductoPedido productoPedidoGuardado = repoProductoPedido.save(productoPedido);
            ProductoPedidoResponse productoPedidoResponse = new ProductoPedidoResponse(productoPedidoGuardado.getId(), pedidoGuardado.getId(), productoResponse, productoPedidoGuardado.getCantidad());
            listaProductoPedido.add(productoPedidoGuardado);
            listaProductoPedidoResponse.add(productoPedidoResponse);        
        }

        pedidoGuardado.setListaProductosPedidos(listaProductoPedido);
        cliente.getPedidos().add(pedidoGuardado);

        List<Long> idPedidos = new ArrayList<>();

        for(Pedido p : cliente.getPedidos()){
            idPedidos.add(p.getId());
        }

        ClienteResponse clienteResponse = new ClienteResponse(cliente.getId(), cliente.getNombre(), cliente.getDni(), idPedidos);

        return new PedidoResponse(pedidoGuardado.getId(), clienteResponse, listaProductoPedidoResponse);
    }

    @Override
    public PedidoResponse buscarPedido(Long idPedido) {
        Pedido pedido = repoPedido.findById(idPedido).orElseThrow(() -> new PedidoNoEncontradoException("El pedido con ID " + idPedido + " no existe."));
        Cliente cliente = pedido.getCliente();

        List<Pedido> listaPedidos = cliente.getPedidos();
        List<Long> idPedidos = new ArrayList<>();

        for(Pedido p : listaPedidos){
            idPedidos.add(p.getId());      
        }

        List<ProductoPedidoResponse> listaProductoPedidoResponse = new ArrayList<>();

        for(ProductoPedido pp : pedido.getListaProductosPedidos()){
            Producto producto = pp.getProducto();
            ProductoResponse productoResponse = new ProductoResponse(producto.getId(), producto.getNombre(), producto.getOrigen(), producto.getPrecio());
            ProductoPedidoResponse productoPedidoResponse = new ProductoPedidoResponse(pp.getId(), idPedido, productoResponse, pp.getCantidad());
            listaProductoPedidoResponse.add(productoPedidoResponse);
        }

        ClienteResponse clienteResponse = new ClienteResponse(pedido.getCliente().getId(), pedido.getCliente().getNombre(), pedido.getCliente().getDni(), idPedidos);

        return new PedidoResponse(idPedido, clienteResponse, listaProductoPedidoResponse);
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
            List<Long> idPedidos = new ArrayList<>();
            Cliente cliente = p.getCliente();
            for(Pedido q : cliente.getPedidos()){
                idPedidos.add(q.getId());
            }

            List<ProductoPedidoResponse> listaProductoPedidoResponse = new ArrayList<>();
            List<ProductoPedido> listaProductoPedido = p.getListaProductosPedidos();

            for(ProductoPedido pp : listaProductoPedido){
                Producto producto = pp.getProducto();
                ProductoResponse productoResponse = new ProductoResponse(producto.getId(), producto.getNombre(), producto.getOrigen(), producto.getPrecio());
                ProductoPedidoResponse productoPedidoResponse = new ProductoPedidoResponse(pp.getId(), p.getId(), productoResponse, pp.getCantidad());
                listaProductoPedidoResponse.add(productoPedidoResponse);
            }

            ClienteResponse respuestaCliente = new ClienteResponse(p.getCliente().getId(), p.getCliente().getNombre(), p.getCliente().getDni(), idPedidos);
            PedidoResponse respuestaPedido = new PedidoResponse(p.getId(), respuestaCliente, listaProductoPedidoResponse);
            pedidos.add(respuestaPedido);
        }
        return pedidos;
    }

    @Override
    public List<PedidoResponse> pedidosCliente(Long idCliente) {
        Cliente cliente = repoCliente.findById(idCliente).orElseThrow(() -> new ClienteNoEncontradoException("El cliente con ID " + idCliente + " no existe."));
        List<Pedido> listaPedido = cliente.getPedidos();
        List<Long> idPedidos = new ArrayList<>();

        for(Pedido p : listaPedido){
            idPedidos.add(p.getId());
        }

        ClienteResponse respuestaCliente = new ClienteResponse(idCliente, cliente.getNombre(), cliente.getDni(), idPedidos);
        List<PedidoResponse> respuesta = new ArrayList<>();

        for(Pedido p : listaPedido){
            List<ProductoPedido> listaProductoPedido = p.getListaProductosPedidos();
            List<ProductoPedidoResponse> listaProductoPedidoResponse = new ArrayList<>();
            for(ProductoPedido pp : listaProductoPedido){
                Producto producto = pp.getProducto();
                ProductoResponse productoResponse = new ProductoResponse(producto.getId(), producto.getNombre(), producto.getOrigen(), producto.getPrecio());
                ProductoPedidoResponse productoPedidoResponse = new ProductoPedidoResponse(pp.getId(), p.getId(), productoResponse, pp.getCantidad());
                listaProductoPedidoResponse.add(productoPedidoResponse);
            }
            PedidoResponse pedidoResponse = new PedidoResponse(p.getId(), respuestaCliente, listaProductoPedidoResponse);
            respuesta.add(pedidoResponse);
        }

        return respuesta;
    }

    @Override
    public Set<PedidoResponse> pedidosProducto(Long idProducto) { 
        //PedidoResponse:
            //ClienteResponse ---> List<Long>idPedidos
            //List<ProductoPedidoResponse> ---> ProductoPedidoResponse --->idPedido, ProductoResponse, cant

        if(!repoProducto.existsById(idProducto)){
            throw new ProductoNoEncontradoException("El producto con ID " + idProducto + " no existe");
        }
        
        List<ProductoPedido> listaProductoPedido = repoProductoPedido.findByProductoId(idProducto);

        Set<PedidoResponse> respuesta = new HashSet<>();
        

        for(ProductoPedido pp : listaProductoPedido){
            Pedido pedido = pp.getPedido();
            List<ProductoPedido> listaProductoPedido2 = pedido.getListaProductosPedidos();
            Cliente cliente = pedido.getCliente();

            List<Long> idPedidos = new ArrayList<>();

            for(Pedido p : cliente.getPedidos()){
                idPedidos.add(p.getId());
            }

            ClienteResponse clienteResponse = new ClienteResponse(cliente.getId(), cliente.getNombre(), cliente.getDni(), idPedidos);
            List<ProductoPedidoResponse> listaProductoPedidoResponse = new ArrayList<>(); 
            
            for(ProductoPedido pp2 : listaProductoPedido2){
                Producto producto = pp2.getProducto();
                ProductoResponse productoResponse = new ProductoResponse(producto.getId(), producto.getNombre(), producto.getOrigen(), producto.getPrecio());
                ProductoPedidoResponse productoPedidoResponse = new ProductoPedidoResponse(pp2.getId(), pp2.getPedido().getId(), productoResponse, pp2.getCantidad());
                listaProductoPedidoResponse.add(productoPedidoResponse);
            }
            PedidoResponse pedidoResponse = new PedidoResponse(pedido.getId(), clienteResponse, listaProductoPedidoResponse);
            respuesta.add(pedidoResponse);
        }
        
        return respuesta;
    }

}
