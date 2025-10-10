package com.hector.pedidos.dto.response;

import java.util.List;

public class PedidoResponse {
    private Long id;
    private ClienteResponse cliente;
    private List<ProductoPedidoResponse> listaProductoPedido;
    
    public PedidoResponse(Long id, ClienteResponse cliente, List<ProductoPedidoResponse> listaProductoPedido) {
        this.id = id;
        this.cliente = cliente;
        this.listaProductoPedido = listaProductoPedido;
    }

    public Long getId() {
        return id;
    }

    public ClienteResponse getCliente() {
        return cliente;
    }

    public List<ProductoPedidoResponse> getListaProductoPedido() {
        return listaProductoPedido;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PedidoResponse other = (PedidoResponse) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    
}
