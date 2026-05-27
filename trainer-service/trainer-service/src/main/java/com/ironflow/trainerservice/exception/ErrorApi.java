package com.ironflow.trainerservice.exception;

import java.time.LocalDateTime;

public record ErrorApi(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
