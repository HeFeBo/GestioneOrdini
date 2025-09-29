package com.hector.pedidos.dto.response;

public class ProductoResponse {
    private Long id;
    private String nombre;
    private String origen;
    private double precio;

    public ProductoResponse(Long id, String nombre, String origen, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.origen = origen;
        this.precio = precio;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getOrigen() {
        return origen;
    }

    public double getPrecio() {
        return precio;
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
        ProductoResponse other = (ProductoResponse) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    

}
