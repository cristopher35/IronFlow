package com.ironflow.branchservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorApi> manejarNoEncontrado(RecursoNoEncontradoException exception, HttpServletRequest request) {
        return construirError(HttpStatus.NOT_FOUND, exception.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorApi> manejarSolicitudIncorrecta(Exception exception, HttpServletRequest request) {
        return construirError(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorApi> manejarValidacion(MethodArgumentNotValidException exception, HttpServletRequest request) {
        Map<String, String> fields = new LinkedHashMap<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            fields.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return construirError(HttpStatus.BAD_REQUEST, "Datos de entrada inválidos", request.getRequestURI(), fields);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorApi> manejarInesperado(Exception exception, HttpServletRequest request) {
        return construirError(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrio un error inesperado en el servicio", request.getRequestURI(), null);
    }

    private ResponseEntity<ErrorApi> construirError(HttpStatus status, String message, String path, Map<String, String> fields) {
        ErrorApi error = new ErrorApi(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                fields
        );

        return ResponseEntity.status(status).body(error);
    }
}
