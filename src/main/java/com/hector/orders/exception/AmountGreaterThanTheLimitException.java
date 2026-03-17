package com.hector.orders.exception;

public class AmountGreaterThanTheLimitException extends RuntimeException{
    public AmountGreaterThanTheLimitException(String message){
        super(message);
    }
}
