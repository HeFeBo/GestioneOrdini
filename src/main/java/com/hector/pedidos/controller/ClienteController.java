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

import com.hector.pedidos.dto.request.ClienteRequest;
import com.hector.pedidos.dto.response.ClienteResponse;
import com.hector.pedidos.service.ClienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private final ClienteService clienteServicio;
    
    public ClienteController(@Qualifier("ServicioCliente") ClienteService clienteServicio) {
        this.clienteServicio = clienteServicio;
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listarClientes(){
        return ResponseEntity.ok(clienteServicio.listarClientes());
    }
    
    @GetMapping("/{idcliente}")
    public ResponseEntity<ClienteResponse> buscarCliente(@PathVariable("idcliente") Long idCliente){
        return ResponseEntity.ok(clienteServicio.buscarCliente(idCliente));
    }

    @GetMapping("/pedido/{idpedido}")
    public ResponseEntity<ClienteResponse> buscarClientePedido(@PathVariable("idpedido") Long idPedido){
        return ResponseEntity.ok(clienteServicio.buscarClientePedido(idPedido));
    }

    @GetMapping("/producto/{idproducto}")
    public ResponseEntity<Set<ClienteResponse>> buscarClientesProducto(@PathVariable("idproducto") Long idProducto){
        return ResponseEntity.ok(clienteServicio.buscarClientesProducto(idProducto));
    }

    @DeleteMapping("/{idcliente}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable("idcliente") Long idCliente){
        clienteServicio.eliminarCliente(idCliente);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> registrarCliente(@Valid @RequestBody ClienteRequest dto){
        return ResponseEntity.status(201).body(clienteServicio.registrarCliente(dto));
    }

}
