package com.authentication.dto;

import java.time.LocalDateTime;

public record ApiResponse(String message, LocalDateTime localDateTime, boolean isSuccess, Object object) {
}
