package cl.duocuc.crmenesesn.ironflow.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorApi(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> fields
) {}
