package com.authentication.exceptions.handlers;

import com.authentication.dto.ApiResponse;
import com.authentication.exceptions.EmailNotRegisteredException;
import com.authentication.exceptions.EmailNotVerifiedException;
import com.authentication.exceptions.InternalServerException;
import com.authentication.exceptions.UnAuthorizedAccessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailNotRegisteredException.class)
    public ResponseEntity<ApiResponse> emailNotRegisteredHandler(EmailNotRegisteredException exception) {
        log.error(String.format("Error message: %s", exception.getMessage()));
        var response = new ApiResponse(exception.getMessage(), "fail", null);
        return new ResponseEntity<>(response, exception.getHttpStatusCode());
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<ApiResponse> emailNotVerifedHandler(EmailNotVerifiedException exception) {
        log.error(String.format("Error message: %s", exception.getMessage()));
        var response = new ApiResponse(exception.getMessage(), "fail", null);
        return new ResponseEntity<>(response, exception.getHttpStatusCode());
    }

    @ExceptionHandler(UnAuthorizedAccessException.class)
    public ResponseEntity<ApiResponse> unAuthorizedAccessHandler(UnAuthorizedAccessException exception) {
        log.error(String.format("Error message: %s", exception.getMessage()));
        var response = new ApiResponse(exception.getMessage(), "error", null);
        return new ResponseEntity<>(response, exception.getHttpStatusCode());
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ApiResponse> internalServerErrorAccessHandler(InternalServerException exception) {
        log.error(String.format("Error message: %s", exception.getMessage()));
        var response = new ApiResponse(exception.getMessage(), "error", null);
        return new ResponseEntity<>(response, exception.getHttpStatusCode());
    }
}
