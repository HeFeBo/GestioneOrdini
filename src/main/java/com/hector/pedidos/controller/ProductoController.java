package com.hector.pedidos.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hector.pedidos.dto.request.ProductoRequest;
import com.hector.pedidos.dto.response.ProductoResponse;
import com.hector.pedidos.service.ProductoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    private final ProductoService productoServicio;

    public ProductoController(@Qualifier("ServicioProducto") ProductoService productoServicio) {
        this.productoServicio = productoServicio;
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listarProductos() {
        return ResponseEntity.ok(productoServicio.listarProductos());
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> registrarProducto(@Valid @RequestBody ProductoRequest dto) {
        return ResponseEntity.status(201).body(productoServicio.registrarProducto(dto));
    }

    @GetMapping("/{idproducto}")
    public ResponseEntity<ProductoResponse> buscarProducto(@PathVariable("idproducto") Long idProducto) {
        return ResponseEntity.ok(productoServicio.buscarProducto(idProducto));
    }

    @DeleteMapping("/{idproducto}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable("idproducto") Long idProducto) {
        productoServicio.eliminarProducto(idProducto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pedido/{idpedido}")
    public ResponseEntity<List<ProductoResponse>> productosPedido(@PathVariable("idpedido") Long idPedido) {
        return ResponseEntity.ok(productoServicio.productosPedido(idPedido));
    }

    @GetMapping("/cliente/{idcliente}")
    public ResponseEntity<Set<ProductoResponse>> productosCliente(@PathVariable("idcliente") Long idCliente) {
        return ResponseEntity.ok(productoServicio.productosCliente(idCliente));
    }

}
