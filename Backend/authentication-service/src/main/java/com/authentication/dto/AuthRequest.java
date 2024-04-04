package com.authentication.dto;


public record AuthRequest(String email, String password, int pin) {
}
