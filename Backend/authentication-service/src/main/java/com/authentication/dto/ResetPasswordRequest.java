package com.authentication.dto;

public record ResetPasswordRequest(String email, String oldPassword, String newPassword) {}
