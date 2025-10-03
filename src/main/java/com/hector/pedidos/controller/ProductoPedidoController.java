package com.hector.pedidos.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hector.pedidos.dto.request.ProductoPedidoRequest;
import com.hector.pedidos.dto.response.ProductoPedidoResponse;
import com.hector.pedidos.dto.response.ProductoResponse;
import com.hector.pedidos.service.ProductoPedidoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/producto_pedido")
public class ProductoPedidoController {
    private final ProductoPedidoService productoPedidoServicio;

    public ProductoPedidoController(ProductoPedidoService productoPedidoServicio) {
        this.productoPedidoServicio = productoPedidoServicio;
    }

    @GetMapping()
    public ResponseEntity<List<ProductoPedidoResponse>> listarProductoPedido(){
        return ResponseEntity.ok(productoPedidoServicio.listarProductoPedido());
    }

    @PostMapping
    public ResponseEntity<ProductoPedidoResponse> agregarProductoPedido(@Valid @RequestBody ProductoPedidoRequest dto){
        return ResponseEntity.status(201).body(productoPedidoServicio.agregarProductoPedido(dto));
    }

    @GetMapping("/productos_vendidos")
    public ResponseEntity<Set<ProductoResponse>> listarProductosVendidos(){
        return ResponseEntity.ok(productoPedidoServicio.listarProductosVendidos());
    }

    @GetMapping("/productos_vendidos/pedido/{idpedido}")
    public ResponseEntity<List<ProductoPedidoResponse>> listarProductosVendidosPorPedido(@PathVariable("idpedido") Long idPedido){
        return ResponseEntity.ok(productoPedidoServicio.listarProductosVendidosPorPedido(idPedido));
    }
    
    @GetMapping("/productos_mas_vendidos/{puestos}")
    public ResponseEntity<List<ProductoResponse>> productosMasVendidos(@PathVariable("puestos") Integer cantProd){
        return ResponseEntity.ok(productoPedidoServicio.productosMasVendidos(cantProd));
    }

    @GetMapping("/venta_total")
    public ResponseEntity<Double> ventaTotalGenerada(){
        return ResponseEntity.ok(productoPedidoServicio.ventaTotalGenerada());
    }

    @GetMapping("/venta_total/pedido/{idpedido}")
    public ResponseEntity<Double> ventaTotalPedido(@PathVariable("idpedido") Long idPedido){
        return ResponseEntity.ok(productoPedidoServicio.ventaTotalPedido(idPedido));
    }

    @GetMapping("/venta_total/cliente/{idcliente}")
    public ResponseEntity<Double> ventaTotalCliente(@PathVariable("idcliente") Long idCliente){
        return ResponseEntity.ok(productoPedidoServicio.ventaTotalCliente(idCliente));
    }

    @GetMapping("/venta_total/producto/{idproducto}")
    public ResponseEntity<Double> ventaTotalProducto(@PathVariable("idproducto") Long idProducto){
        return ResponseEntity.ok(productoPedidoServicio.ventaTotalProducto(idProducto));
    }

    @GetMapping("/promedio_venta_de_pedidos")
    public ResponseEntity<Double> promedioVentaDePedidos(){
        return ResponseEntity.ok(productoPedidoServicio.promedioVentaDePedidos());
    }

    @GetMapping("/total_unidades_vendidas")
    public ResponseEntity<Long> totalUnidadesVendidas(){
        return ResponseEntity.ok(productoPedidoServicio.totalUnidadesVendidas());
    }

    @GetMapping("/total_unidades_vendidas/pedido/{idpedido}")
    public ResponseEntity<Integer> totalUnidadesVendidasPorPedido(@PathVariable("idpedido") Long idPedido){
        return ResponseEntity.ok(productoPedidoServicio.totalUnidadesVendidasPorPedido(idPedido));
    }

    @GetMapping("/total_unidades_vendidas/cliente/{idcliente}")
    public ResponseEntity<Integer> totalUnidadesVendidasPorCliente(@PathVariable("idcliente") Long idCliente){
        return ResponseEntity.ok(productoPedidoServicio.totalUnidadesVendidasPorCliente(idCliente));
    }

    @GetMapping("/total_unidades_vendidas/producto/{idproducto}")
    public ResponseEntity<Integer> totalUnidadesVendidasPorProducto(@PathVariable("idproducto") Long idProducto){
        return ResponseEntity.ok(productoPedidoServicio.totalUnidadesVendidasPorProducto(idProducto));
    }

}
