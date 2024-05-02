package com.authentication.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatusCode;

/**
 * This exception is raised the user enters the email address for services such as login, etc. which is not verified by
 * the system and the user.
 *
 * @author rsmalani
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EmailNotVerifiedException extends RuntimeException {

    private final String message;
    private final HttpStatusCode httpStatusCode;
}
