package com.flatrock.common.errors;

import org.springframework.http.HttpStatus;

import java.util.Map;

public interface ParameterizedException{
    HttpStatus getHttpStatus();
    Map<String, Object> getParameterMap();
}
