package com.authentication.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatusCode;

/**
 * This exception is raised the user enters the email address for services other than registration which is not
 * registered in the system.
 * @author rsmalani
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EmailNotRegisteredException extends RuntimeException {
    private final String message;
    private final HttpStatusCode httpStatusCode;
}
