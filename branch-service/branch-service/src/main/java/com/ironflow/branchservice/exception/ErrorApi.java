package com.ironflow.branchservice.exception;

import java.time.LocalDateTime;

public record ErrorApi(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
