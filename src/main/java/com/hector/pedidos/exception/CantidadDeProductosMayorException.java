package com.hector.pedidos.exception;

public class CantidadDeProductosMayorException extends RuntimeException{
    public CantidadDeProductosMayorException(String mensaje){
        super(mensaje);
    }
}
