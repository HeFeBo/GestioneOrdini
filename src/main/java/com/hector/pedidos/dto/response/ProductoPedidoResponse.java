package com.hector.pedidos.dto.response;

public class ProductoPedidoResponse {
    private Long id;
    private Long idPedido;
    private ProductoResponse producto;
    private Integer cantidad;

    public ProductoPedidoResponse(Long id, Long idPedido, ProductoResponse producto, Integer cantidad) {
        this.id = id;
        this.idPedido = idPedido;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Long getId() {
        return id;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public ProductoResponse getProducto() {
        return producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

}
