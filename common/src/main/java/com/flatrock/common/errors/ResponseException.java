package com.flatrock.common.errors;

public abstract class ResponseException extends RuntimeException implements ParameterizedException{

    protected ResponseException(String message) {
        super(message);
    }
}
