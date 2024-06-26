package com.authentication.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatusCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UnAuthorizedAccessException extends RuntimeException {
    private final String message;
    private final int httpStatusCode;
}
