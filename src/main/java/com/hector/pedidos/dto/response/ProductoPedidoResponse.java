package com.hector.pedidos.dto.response;

public class ProductoPedidoResponse {
    private Long id;
    private PedidoResponse pedido;
    private ProductoResponse producto;
    private Integer cantidad;

    public ProductoPedidoResponse(Long id, PedidoResponse pedido, ProductoResponse producto, Integer cantidad) {
        this.id = id;
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Long getId() {
        return id;
    }

    public PedidoResponse getPedido() {
        return pedido;
    }

    public ProductoResponse getProducto() {
        return producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

}
