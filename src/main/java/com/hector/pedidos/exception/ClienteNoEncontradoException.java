package com.hector.pedidos.exception;

public class ClienteNoEncontradoException extends RuntimeException{
    public ClienteNoEncontradoException(String mensaje){
        super(mensaje);
    }
}
