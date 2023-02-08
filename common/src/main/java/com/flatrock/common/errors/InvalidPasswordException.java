package com.flatrock.common.errors;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class InvalidPasswordException extends ResponseException implements ParameterizedException{

    private static final long serialVersionUID = 1L;

    public InvalidPasswordException() {
        super("Incorrect password");
    }


    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public Map<String, Object> getParameterMap() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("timestamp", LocalDateTime.now());
        parameters.put("message", getMessage() );
        parameters.put("params", "user");
        parameters.put("status", HttpStatus.BAD_REQUEST);
        return parameters;
    }
}
