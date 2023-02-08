package com.flatrock.common.errors;

public abstract class ResponseException extends RuntimeException implements ParameterizedException{

    public ResponseException(String message) {
        super(message);
    }
}
