package com.hector.pedidos.dto.request;

import jakarta.validation.constraints.Min;

public class ProductoPedidoRequest {
    private Long idPedido;
    private Long idProducto;

    @Min(1)
    private Integer cantidad;

    public Long getIdPedido() {
        return idPedido;
    }
    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }
    public Long getIdProducto() {
        return idProducto;
    }
    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }
    public Integer getCantidad() {
        return cantidad;
    }
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

}
