package com.hector.pedidos.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ClienteRequest {
    @NotBlank(message = "El nombre no puede quedar vacio")
    private String nombre;

    @NotBlank(message = "El dni no puede quedar vacio")
    private String dni;

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDni() {
        return dni;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }

}
