package com.flatrock.common.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
public class BadRequestAlertException extends ResponseException implements ParameterizedException{

    private static final long serialVersionUID = 1L;

    private final String entityName;
    private final String errorKey;
    private static final HttpStatus status = HttpStatus.BAD_REQUEST;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp = LocalDateTime.now();
    public BadRequestAlertException(String defaultMessage, String entityName, String errorKey) {
        super(defaultMessage);
        this.entityName = entityName;
        this.errorKey = errorKey;
    }


    @Override
    public HttpStatus getHttpStatus() {
        return status;
    }

    public Map<String, Object> getParameterMap() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("timestamp", timestamp);
        parameters.put("message", getMessage());
        parameters.put("params", entityName);
        parameters.put("status", status);
        return parameters;
    }
}
