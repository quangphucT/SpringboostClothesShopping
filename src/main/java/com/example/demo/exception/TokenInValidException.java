package com.example.demo.exception;

public class TokenInValidException extends RuntimeException {
    public TokenInValidException(String message) {
        super(message);
    }
}
