package com.example.apartment_manager.exception;

import com.example.apartment_manager.dto.response.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<?>> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        return new ResponseEntity<>(new CommonResponse<>(400, "Validation failed", errorMessages), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<CommonResponse<?>> handleDataNotFound(DataNotFoundException ex) {
        return new ResponseEntity<>(new CommonResponse<>(404, ex.getMessage(), null), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResponse<?>> handleInvalidJson(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(new CommonResponse<>(400, "Malformed JSON request", null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResponse<?>> handleIllegalArgument(IllegalArgumentException ex) {
        return new ResponseEntity<>(new CommonResponse<>(400, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<?>> handleAllOtherExceptions(Exception ex) {
        ex.printStackTrace();
        System.out.println(ex.getMessage());
        return new ResponseEntity<>(new CommonResponse<>(500, "Internal Server Error: " + ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
