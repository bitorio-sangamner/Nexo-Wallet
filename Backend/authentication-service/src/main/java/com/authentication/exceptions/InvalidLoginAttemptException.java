package com.authentication.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatusCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InvalidLoginAttemptException extends RuntimeException {
    private final String message;
    private final HttpStatusCode httpStatusCode;
}
