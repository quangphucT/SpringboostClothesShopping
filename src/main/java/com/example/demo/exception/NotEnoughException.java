package com.example.demo.exception;

public class NotEnoughException extends RuntimeException{
    public NotEnoughException(String message) {
        super(message);
    }
}
