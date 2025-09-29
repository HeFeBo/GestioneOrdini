package com.hector.pedidos.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hector.pedidos.model.Producto;
import com.hector.pedidos.model.Cliente;
import com.hector.pedidos.model.Pedido;
import com.hector.pedidos.dto.request.ProductoRequest;
import com.hector.pedidos.dto.response.ProductoResponse;
import com.hector.pedidos.repository.ClienteRepository;
import com.hector.pedidos.repository.PedidoRepository;
import com.hector.pedidos.repository.ProductoRepository;
import com.hector.pedidos.service.ProductoService;

@Qualifier("ServicioProducto")
@Service
public class ProductoServiceImpl implements ProductoService {
    private final ClienteRepository repoCliente;
    private final ProductoRepository repoProducto;
    private final PedidoRepository repoPedido;
    
    public ProductoServiceImpl(ClienteRepository repoCliente, ProductoRepository repoProducto,
            PedidoRepository repoPedido) {
        this.repoCliente = repoCliente;
        this.repoProducto = repoProducto;
        this.repoPedido = repoPedido;
    }

    @Override
    public ProductoResponse buscarProducto(long idProducto) {
        Producto producto = repoProducto.findById(idProducto).orElseThrow(() -> new RuntimeException("No se encontro el producto"));
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
        Cliente cliente = repoCliente.findById(idCliente).orElseThrow(() -> new RuntimeException("No se encontro el cliente"));
        List<Pedido> pedidos = cliente.getPedidos();
        Set<ProductoResponse> productosRespuesta = new HashSet<>();
        for(Pedido p : pedidos){
            for(Producto c : p.getProductos()){
                ProductoResponse respuesta = new ProductoResponse(c.getId(), c.getNombre(), c.getOrigen(), c.getPrecio());
                productosRespuesta.add(respuesta);//Aqui se estan repitiendo productos... mejor crear un Set.
                                                  //Ver la posibilidad de crear otra entidad llamada Producto_Pedidos para considerar la cantidad por producto en un pedido.
            }
        }
        return productosRespuesta;
    }

    @Override
    public List<ProductoResponse> productosPedido(Long idPedido) {
        Pedido pedido = repoPedido.findById(idPedido).orElseThrow(() -> new RuntimeException("No se encontro el pedido."));
        List<Producto> productos = pedido.getProductos();
        List<ProductoResponse> productosRespuesta = new ArrayList<>();
        for(Producto p : productos){
            ProductoResponse respuesta = new ProductoResponse(p.getId(), p.getNombre(), p.getOrigen(), p.getPrecio());
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
