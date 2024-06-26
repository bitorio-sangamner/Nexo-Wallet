package com.gateway.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatusCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class AuthenticationException extends RuntimeException{

    private final String message;
    private final int httpStatusCode;
}
