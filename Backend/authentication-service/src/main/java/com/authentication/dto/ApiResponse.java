package com.authentication.dto;

import java.time.LocalDateTime;

public record ApiResponse(String message, String status, Object object) {
}
