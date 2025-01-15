package com.librarymanagement.LibraryManagement;

import com.librarymanagement.LibraryManagement.author.exception.AuthorAlreadyExistsException;
import com.librarymanagement.LibraryManagement.author.exception.AuthorNotFoundException;
import com.librarymanagement.LibraryManagement.book.exception.BookAlreadyExistsException;
import com.librarymanagement.LibraryManagement.book.exception.BookNotFoundException;
import com.librarymanagement.LibraryManagement.category.exception.CategoryNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> illegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exception(Exception e) {
        return new ResponseEntity<>("An unexpected error occured: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Author exceptions
    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<String> handleAuthorNotFoundException(AuthorNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthorAlreadyExistsException.class)
    public ResponseEntity<String> handleAuthorAlreadyExistsException(AuthorAlreadyExistsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    // Book exceptions
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<String> bookNotFoundException(BookNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookAlreadyExistsException.class)
    public ResponseEntity<String> bookAlreadyExistsException(BookAlreadyExistsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<String> handleCategoryNotFoundException(CategoryNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Walidacja (np. @Valid)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        String rootMessage = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();

        String errorMsg = "Błąd integralności danych.";
        if (rootMessage != null && rootMessage.contains("Duplicate entry")) {
            errorMsg = "Książka z tym ISBN już istnieje w bazie danych.";
        } else if (rootMessage != null && rootMessage.contains("cannot be null")) {
            errorMsg = "Jedno z wymaganych pól jest puste (null).";
        }

        errorDetails.put("message", errorMsg);
        errorDetails.put("status", HttpStatus.CONFLICT.value());
        errorDetails.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDetails);
    }
}