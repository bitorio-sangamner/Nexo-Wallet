package com.authentication.dto;

/**
 * This record is used by the user for log in to the system.
 *
 * @param email    Email address of the user registered in the system.
 * @param password Password for the account registered.
 * @param pin      Numeric pin for the account registered.
 * @author rsmalani
 */
public record AuthRequest(String email, String password, int pin) {
}
