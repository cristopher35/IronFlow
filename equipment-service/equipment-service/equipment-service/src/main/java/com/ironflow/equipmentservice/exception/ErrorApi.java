package com.ironflow.equipmentservice.exception;

import java.time.LocalDateTime;

public record ErrorApi(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
