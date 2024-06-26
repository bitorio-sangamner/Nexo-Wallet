package com.gateway.dto;

public record ApiResponse(String message, String status, Object object) {
}
