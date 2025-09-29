package com.hector.pedidos.exception;

public class ProductoNoEncontradoException extends RuntimeException{
    public ProductoNoEncontradoException(String mensaje){
        super(mensaje);
    }
}
