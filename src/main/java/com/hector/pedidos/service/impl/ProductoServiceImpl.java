package com.hector.pedidos.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hector.pedidos.model.Producto;
import com.hector.pedidos.model.ProductoPedido;
import com.hector.pedidos.model.Cliente;
import com.hector.pedidos.model.Pedido;
import com.hector.pedidos.dto.request.ProductoRequest;
import com.hector.pedidos.dto.response.ProductoResponse;
import com.hector.pedidos.exception.ClienteNoEncontradoException;
import com.hector.pedidos.exception.PedidoNoEncontradoException;
import com.hector.pedidos.exception.ProductoNoEncontradoException;
import com.hector.pedidos.repository.ClienteRepository;
import com.hector.pedidos.repository.PedidoRepository;
import com.hector.pedidos.repository.ProductoPedidoRepository;
import com.hector.pedidos.repository.ProductoRepository;
import com.hector.pedidos.service.ProductoService;

@Qualifier("ServicioProducto")
@Service
public class ProductoServiceImpl implements ProductoService {
    private final ClienteRepository repoCliente;
    private final ProductoRepository repoProducto;
    private final PedidoRepository repoPedido;
    private final ProductoPedidoRepository repoProductoPedido;
    
    public ProductoServiceImpl(ClienteRepository repoCliente, ProductoRepository repoProducto,
            PedidoRepository repoPedido, ProductoPedidoRepository repoProductoPedido) {
        this.repoCliente = repoCliente;
        this.repoProducto = repoProducto;
        this.repoPedido = repoPedido;
        this.repoProductoPedido = repoProductoPedido;
    }

    @Override
    public ProductoResponse buscarProducto(long idProducto) {
        Producto producto = repoProducto.findById(idProducto).orElseThrow(() -> new ProductoNoEncontradoException("El producto con ID " + idProducto + " no existe."));
        return new ProductoResponse(producto.getId(), producto.getNombre(), producto.getOrigen(), producto.getPrecio());
    }

    @Override
    public void eliminarProducto(Long idProducto) {
        repoProducto.deleteById(idProducto);
    }

    @Override
    public List<ProductoResponse> listarProductos() {
        return repoProducto.findAll().stream().map(p -> new ProductoResponse(p.getId(), p.getNombre(), p.getOrigen(), p.getPrecio())).collect(Collectors.toList());
    }

    @Override
    public Set<ProductoResponse> productosCliente(Long idCliente) {
        Cliente cliente = repoCliente.findById(idCliente).orElseThrow(() -> new ClienteNoEncontradoException("El cliente con ID " + idCliente + " no existe."));
        List<Pedido> pedidos = cliente.getPedidos();
        Set<ProductoResponse> productosRespuesta = new HashSet<>();

        for(Pedido p : pedidos){
            List<ProductoPedido> productosPedidos = repoProductoPedido.findByPedidoId(p.getId());
            for(ProductoPedido c : productosPedidos){
                Producto producto = c.getProducto();
                ProductoResponse respuesta = new ProductoResponse(producto.getId(), producto.getNombre(), producto.getOrigen(), producto.getPrecio());
                productosRespuesta.add(respuesta);                                   
            }
        }
        return productosRespuesta;
    }

    @Override
    public List<ProductoResponse> productosPedido(Long idPedido) {
        if (!repoPedido.existsById(idPedido)) {
            throw new PedidoNoEncontradoException("El pedido con ID " + idPedido + " no existe");
        }
        
        List<ProductoPedido> productosPedidos = repoProductoPedido.findByPedidoId(idPedido);

        List<ProductoResponse> productosRespuesta = new ArrayList<>();
        for(ProductoPedido pp : productosPedidos){
            Producto producto = pp.getProducto();
            ProductoResponse respuesta = new ProductoResponse(producto.getId(), producto.getNombre(), producto.getOrigen(), producto.getPrecio());
            productosRespuesta.add(respuesta);
        }
        return productosRespuesta;
    }

    @Override
    public ProductoResponse registrarProducto(ProductoRequest dto) {
        Producto producto = new Producto(dto.getNombre(), dto.getOrigen(), dto.getPrecio());
        Producto guardado = repoProducto.save(producto);

        return new ProductoResponse(guardado.getId(), guardado.getNombre(), guardado.getOrigen(), guardado.getPrecio());
    }

}
