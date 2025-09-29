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

import com.hector.pedidos.dto.request.PedidoRequest;
import com.hector.pedidos.dto.response.PedidoResponse;
import com.hector.pedidos.service.PedidoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoService pedidoServicio;

    public PedidoController(@Qualifier("ServicioPedido") PedidoService pedidoServicio) {
        this.pedidoServicio = pedidoServicio;
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listarPedidos() {
        return ResponseEntity.ok(pedidoServicio.listarPedidos());
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> agregarPedido(@Valid @RequestBody PedidoRequest dto) {
        return ResponseEntity.status(201).body(pedidoServicio.agregarPedido(dto));
    }

    @GetMapping("/{idpedido}")
    public ResponseEntity<PedidoResponse> buscarPedido(@PathVariable("idpedido") Long idPedido) {
        return ResponseEntity.ok(pedidoServicio.buscarPedido(idPedido));
    }

    @DeleteMapping("/{idpedido}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable("idpedido") Long idPedido) {
        pedidoServicio.eliminarPedido(idPedido);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cliente/{idcliente}")    
    public ResponseEntity<List<PedidoResponse>> pedidosCliente(@PathVariable("idcliente") Long idCliente) {
        return ResponseEntity.ok(pedidoServicio.pedidosCliente(idCliente));
    }

    @GetMapping("/producto/{idproducto}") 
    public ResponseEntity<Set<PedidoResponse>> pedidosProducto(@PathVariable("idproducto") Long idProducto) {
        return ResponseEntity.ok(pedidoServicio.pedidosProducto(idProducto));
    }

}
