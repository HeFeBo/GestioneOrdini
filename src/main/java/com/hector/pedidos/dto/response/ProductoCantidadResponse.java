package com.hector.pedidos.dto.response;

public class ProductoCantidadResponse {
    private Long idProducto;
    private Long cantidad;

    public ProductoCantidadResponse(Long idProducto, Long cantidad) {
        this.idProducto = idProducto;
        this.cantidad = cantidad;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public Long getCantidad() {
        return cantidad;
    }
}
