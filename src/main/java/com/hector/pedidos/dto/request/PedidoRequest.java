package com.hector.pedidos.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public class PedidoRequest {
    private Long idCliente;

    @NotEmpty(message = "Debe contener al menos 1 elemento")
    private List<Long> idProductos;

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public List<Long> getIdProductos() {
        return idProductos;
    }

    public void setIdProductos(List<Long> idProductos) {
        this.idProductos = idProductos;
    }

}
