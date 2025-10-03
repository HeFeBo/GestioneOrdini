package com.hector.pedidos.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hector.pedidos.dto.request.ProductoPedidoRequest;
import com.hector.pedidos.dto.response.ClienteResponse;
import com.hector.pedidos.dto.response.PedidoResponse;
import com.hector.pedidos.dto.response.ProductoCantidadResponse;
import com.hector.pedidos.dto.response.ProductoPedidoResponse;
import com.hector.pedidos.dto.response.ProductoResponse;
import com.hector.pedidos.exception.CantidadDeProductosMayorException;
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
import com.hector.pedidos.service.ProductoPedidoService;

@Qualifier("ServicioProductoPedido")
@Service
public class ProductoPedidoServiceImpl implements ProductoPedidoService{

    private final ClienteRepository repoCliente;
    private final ProductoRepository repoProducto;
    private final PedidoRepository repoPedido;
    private final ProductoPedidoRepository repoProductoPedido;

    public ProductoPedidoServiceImpl(ClienteRepository repoCliente, ProductoRepository repoProducto, PedidoRepository repoPedido, ProductoPedidoRepository repoProductoPedido) {
        this.repoCliente = repoCliente;
        this.repoProducto = repoProducto;
        this.repoPedido = repoPedido;
        this.repoProductoPedido = repoProductoPedido;

    }

    @Override
    public ProductoPedidoResponse agregarProductoPedido(ProductoPedidoRequest dto) {
        Pedido pedido = repoPedido.findById(dto.getIdPedido()).orElseThrow(() -> new PedidoNoEncontradoException("El pedido con ID " + dto.getIdPedido() + " no existe."));
        Cliente cliente = pedido.getCliente();
        Producto producto = repoProducto.findById(dto.getIdProducto()).orElseThrow(() -> new ProductoNoEncontradoException("El producto con ID " + dto.getIdProducto() + " no existe."));

        ProductoPedido productoPedido = new ProductoPedido(pedido, producto, dto.getCantidad());
        ProductoPedido guardado = repoProductoPedido.save(productoPedido);

        
        ClienteResponse clienteResponse = new ClienteResponse(cliente.getId(), cliente.getNombre(), cliente.getDni());
        ProductoResponse productoResponse = new ProductoResponse(producto.getId(), producto.getNombre(), producto.getOrigen(), producto.getPrecio());
        PedidoResponse pedidoResponse = new PedidoResponse(pedido.getId(), clienteResponse);

        return new ProductoPedidoResponse(guardado.getId(), pedidoResponse, productoResponse, guardado.getCantidad());
    }

    @Override
    public Double ventaTotalCliente(Long idCliente) {
        Cliente cliente = repoCliente.findById(idCliente).orElseThrow(() -> new ClienteNoEncontradoException("El cliente con ID " + idCliente + " no existe."));
        List<Pedido> pedidos = cliente.getPedidos();
        double ventaTotal = 0;

        for(Pedido p : pedidos){
            ventaTotal = ventaTotal + ventaTotalPedido(p.getId());
        }

        return ventaTotal;
        
    }

    @Override
    public Double ventaTotalPedido(Long idPedido) {
        Pedido pedido = repoPedido.findById(idPedido).orElseThrow(() -> new PedidoNoEncontradoException("El pedido con ID " + idPedido + " no existe."));
        double venta = 0;
        List<ProductoPedido> listaProductoPedido = repoProductoPedido.findAll();

        for(ProductoPedido pp : listaProductoPedido){
            if(pp.getPedido().equals(pedido)){
                Producto producto = pp.getProducto();
                Integer cantidad = pp.getCantidad();
                Double precio = producto.getPrecio();

                venta = venta + cantidad*precio;
            }
        }
        return venta;
    }

    @Override
    public Double ventaTotalProducto(Long idProducto) {
        if(!repoProducto.existsById(idProducto)){
            throw new ProductoNoEncontradoException("El producto con ID " + idProducto + " no existe.");
        }

        double precio = repoProducto.findById(idProducto).orElseThrow().getPrecio();
        int cantidad = totalUnidadesVendidasPorProducto(idProducto);

        return precio*cantidad;
    }

    @Override
    public List<ProductoPedidoResponse> listarProductoPedido() {
        List<ProductoPedido> listaProductoPedido = repoProductoPedido.findAll();
        List<ProductoPedidoResponse> respuesta = new ArrayList<>();

        for(ProductoPedido pp : listaProductoPedido){
            Pedido pedido = pp.getPedido();
            Cliente cliente = pedido.getCliente();
            Producto producto = pp.getProducto();

            ClienteResponse clienteResponse = new ClienteResponse(cliente.getId(), cliente.getNombre(), cliente.getDni());
            ProductoResponse productoResponse = new ProductoResponse(producto.getId(), producto.getNombre(), producto.getOrigen(), producto.getPrecio());

            PedidoResponse pedidoResponse = new PedidoResponse(pedido.getId(), clienteResponse);
            ProductoPedidoResponse productoPedidoResponse = new ProductoPedidoResponse(pp.getId(), pedidoResponse, productoResponse, pp.getCantidad());
            respuesta.add(productoPedidoResponse);
        }

        return respuesta;
    }

    @Override
    public Set<ProductoResponse> listarProductosVendidos() {
        Set<ProductoResponse> listaProductosVendidos = new HashSet<>();
        for(ProductoPedidoResponse p : listarProductoPedido()){
            ProductoResponse producto = p.getProducto();
            listaProductosVendidos.add(producto);
        }    
        return listaProductosVendidos;
    }

    @Override
    public List<ProductoPedidoResponse> listarProductosVendidosPorPedido(Long idPedido) {
        if(!repoPedido.existsById(idPedido)){
            throw new PedidoNoEncontradoException("El pedido con ID " + idPedido + " no existe.");
        }
        List<ProductoPedido> listaProductoPedido = repoProductoPedido.findByPedidoId(idPedido);
        List<ProductoPedidoResponse> respuesta = new ArrayList<>();

        for(ProductoPedido pp : listaProductoPedido){
            Pedido pedido = pp.getPedido();
            Cliente cliente = pedido.getCliente();
            Producto producto = pp.getProducto();

            ClienteResponse clienteResponse = new ClienteResponse(cliente.getId(), cliente.getNombre(), cliente.getDni());
            PedidoResponse pedidoResponse = new PedidoResponse(idPedido, clienteResponse);
            ProductoResponse productoResponse = new ProductoResponse(producto.getId(), producto.getNombre(), producto.getOrigen(), producto.getPrecio());

            ProductoPedidoResponse productoPedidoResponse = new ProductoPedidoResponse(pp.getId(), pedidoResponse, productoResponse, pp.getCantidad());
            respuesta.add(productoPedidoResponse);
        }

        return respuesta;
    }

    @Override
    public List<ProductoResponse> productosMasVendidos(Integer cantProd) {
        List<ProductoCantidadResponse> listaProductoCantidad = repoProductoPedido.findProductosOrdenadosPorCantidad();

        if(listaProductoCantidad.size() < cantProd){
            throw new CantidadDeProductosMayorException("La cantidad ingresada (" + cantProd + ") es mayor a la cantidad de productos registrados.");
        }
        List<ProductoResponse> respuesta = new ArrayList<>();

        for(int i=0; i<cantProd; i++){
            Producto producto = repoProducto.findById(listaProductoCantidad.get(i).getIdProducto()).orElseThrow();
            ProductoResponse productoResponse = new ProductoResponse(producto.getId(), producto.getNombre(), producto.getOrigen(), producto.getPrecio());
            respuesta.add(productoResponse);
        }

        return respuesta;        
    }

    @Override
    public Double promedioVentaDePedidos() {
        List<Pedido> listaPedidos = repoPedido.findAll();
        int cantPedidos = listaPedidos.size();
        double ventaTot = 0;
        
        for(Pedido p: listaPedidos){
            ventaTot = ventaTot + ventaTotalPedido(p.getId());
        }

        return ventaTot/cantPedidos;
    }

    @Override
    public Long totalUnidadesVendidas() {
        List<ProductoCantidadResponse> listaProductoCantidad = repoProductoPedido.findProductosOrdenadosPorCantidad();
        long unidTot = 0;

        for(ProductoCantidadResponse pc : listaProductoCantidad){
            unidTot = unidTot + pc.getCantidad();
        }
        return unidTot;
    }

    @Override
    public Integer totalUnidadesVendidasPorCliente(Long idCliente) {
        Cliente cliente = repoCliente.findById(idCliente).orElseThrow(() -> new ClienteNoEncontradoException("El cliente con ID " + idCliente + " no existe."));
        List<Pedido> listaPedidos = cliente.getPedidos();
        int totalUnidades = 0;

        for(Pedido p : listaPedidos){
            totalUnidades = totalUnidades + totalUnidadesVendidasPorPedido(p.getId());
        }
        
        return totalUnidades;
    }

    @Override
    public Integer totalUnidadesVendidasPorPedido(Long idPedido) {
        Pedido pedido = repoPedido.findById(idPedido).orElseThrow(() -> new PedidoNoEncontradoException("El pedido con ID " + idPedido + " no existe."));
        Integer cantidad = 0;
        List<ProductoPedido> listaProductoPedido = repoProductoPedido.findAll();

        for(ProductoPedido pp : listaProductoPedido){
            if(pp.getPedido().equals(pedido)){
                cantidad = cantidad + pp.getCantidad();
            }
        }
        return cantidad;
    }

    @Override
    public Integer totalUnidadesVendidasPorProducto(Long idProducto) {
        List<ProductoPedido> listaProductoPedido = repoProductoPedido.findByProductoId(idProducto);
        int total = 0;

        for(ProductoPedido pp : listaProductoPedido){
            total = total + pp.getCantidad();
        }
        return total;
    }

    @Override
    public Double ventaTotalGenerada() {
        List<ProductoCantidadResponse> listaProductoCantidad = repoProductoPedido.findProductosOrdenadosPorCantidad();
        double ventaTotal = 0;

        for(ProductoCantidadResponse pc : listaProductoCantidad){
            Producto producto = repoProducto.findById(pc.getIdProducto()).orElseThrow();
            double precio = producto.getPrecio();
            long cantidad = pc.getCantidad();
            ventaTotal = ventaTotal + cantidad*precio;
        }
        return ventaTotal;
    }

}
