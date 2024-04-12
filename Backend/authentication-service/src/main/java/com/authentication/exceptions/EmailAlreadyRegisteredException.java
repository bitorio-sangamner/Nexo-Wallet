package com.authentication.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatusCode;

/**
 * This exception is raised the user enters the email address for registration which is already registered in the
 * system.
 * @author rsmalani
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EmailAlreadyRegisteredException extends RuntimeException {
    private final String message;
    private final HttpStatusCode httpStatusCode;
}
