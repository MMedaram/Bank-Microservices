// package com.bank.account.exception;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.context.request.WebRequest;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Slf4j
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, String>> handleValidationExceptions(
//            MethodArgumentNotValidException ex, WebRequest request) {
//
//        // Only handle runtime requests (ignore SpringDoc startup)
//        if (request == null || request.getDescription(false).contains("v3/api-docs")) {
//            return null; // Skip handling during OpenAPI generation
//        }
//
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getFieldErrors()
//                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
//        return ResponseEntity.badRequest().body(errors);
//    }
//
//
//}
//
