package com.hector.pedidos.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolationException;

public class ManejadorGlobalDeErrores {
    // 1. Validaciones en DTOs con @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarErroresValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errores.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }

    // 2. Validaciones en parámetros simples (@RequestParam, @PathVariable)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<String>> manejarViolaciones(ConstraintViolationException ex) {
        List<String> errores = ex.getConstraintViolations()
            .stream()
            .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
            .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errores);
    }

    // 3. JSON mal formado o tipos incorrectos en el cuerpo
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> manejarJsonMalFormado(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body("El formato del cuerpo de la solicitud es inválido.");
    }

    // 4. Tipos incorrectos en parámetros de URL
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> manejarTipoIncorrecto(MethodArgumentTypeMismatchException ex) {
        String campo = ex.getName();
        Class<?> tipo = ex.getRequiredType();
        String tipoEsperado = tipo != null ? tipo.getSimpleName() : "desconocido";
        return ResponseEntity.badRequest().body("El parámetro '" + campo + "' debe ser de tipo " + tipoEsperado);
    }

    // 5. Parámetros obligatorios faltantes
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> manejarParametroFaltante(MissingServletRequestParameterException ex) {
        return ResponseEntity.badRequest().body("Falta el parámetro obligatorio: " + ex.getParameterName());
    }

    // 6. Ejemplo de excepción personalizada
    @ExceptionHandler(ClienteNoEncontradoException.class)
    public ResponseEntity<String> manejarClienteNoEncontrado(ClienteNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // 7. Fallback general (opcional)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> manejarExcepcionGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error inesperado.");
    }
}
