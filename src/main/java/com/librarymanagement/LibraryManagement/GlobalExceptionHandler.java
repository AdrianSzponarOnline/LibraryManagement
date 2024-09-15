package com.librarymanagement.LibraryManagement.author;

import com.librarymanagement.LibraryManagement.author.exception.AuthorAlreadyExistsException;
import com.librarymanagement.LibraryManagement.author.exception.AuthorNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    // 404
    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<String> handleAuthorNotFoundException(AuthorNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    // 409
    @ExceptionHandler(AuthorAlreadyExistsException.class)
    public ResponseEntity<String> handleAuthorAlreadyExistsException(AuthorAlreadyExistsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>("An unexpected error has occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
