package com.gateway.exception.handlers;

import com.gateway.dto.ApiResponse;
import com.gateway.exception.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthenticationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse> handleAuthorizationHeaderException(final AuthenticationException exception) {
        log.error(String.format("Error message: %s", exception.getMessage()));
        int httpStatusCode = exception.getHttpStatusCode();
        var response = new ApiResponse("The Authorization token is timed out.", exception.getMessage(), httpStatusCode);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(httpStatusCode));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleUnknownException(final Exception exception) {
        log.error(String.format("Error message: %s", exception.getMessage()));
        int httpStatusCode = 500;
        var response = new ApiResponse(null, "error", httpStatusCode);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(httpStatusCode));
    }

}