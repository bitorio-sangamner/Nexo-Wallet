package com.gateway.exception.handlers;

import com.gateway.dto.ApiResponse;
import com.gateway.exception.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthenticationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse> handleAuthorizationHeaderException(final AuthenticationException exception) {
        log.error(String.format("Error message: %s", exception.getMessage()));
        var response = new ApiResponse(exception.getMessage(), LocalDateTime.now(), false, null);
        return new ResponseEntity<>(response, exception.getHttpStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleUnknownException(final Exception exception) {
        log.error(String.format("Error message: %s", exception.getMessage()));
        exception.printStackTrace();
        var response = new ApiResponse(exception.getMessage(), LocalDateTime.now(), false, null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}