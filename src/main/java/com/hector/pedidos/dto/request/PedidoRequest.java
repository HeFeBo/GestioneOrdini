package com.hector.pedidos.dto.request;

import java.util.List;

public class PedidoRequest {
    private Long idCliente;
    private List<ProductoPedidoRequest> listaProductoPedido;
    
    public Long getIdCliente() {
        return idCliente;
    }
    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }
    public List<ProductoPedidoRequest> getListaProductoPedido() {
        return listaProductoPedido;
    }
    public void setListaProductoPedido(List<ProductoPedidoRequest> listaProductoPedido) {
        this.listaProductoPedido = listaProductoPedido;
    }

}
