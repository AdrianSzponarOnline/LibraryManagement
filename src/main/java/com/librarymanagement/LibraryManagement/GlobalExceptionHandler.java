package com.librarymanagement.LibraryManagement;

import com.librarymanagement.LibraryManagement.author.exception.AuthorAlreadyExistsException;
import com.librarymanagement.LibraryManagement.author.exception.AuthorNotFoundException;
import com.librarymanagement.LibraryManagement.book.exception.BookAlreadyExistsException;
import com.librarymanagement.LibraryManagement.book.exception.BookNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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


    // Book exceptions

    // obsługa wyjątku BookNotFoundException (404)
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<String> bookNotFoundException(BookNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    // obsługa wyjątku BookAlreadyExistsException (409)
    @ExceptionHandler(BookAlreadyExistsException.class)
    public ResponseEntity<String> bookAlreadyExistsException(BookAlreadyExistsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }



}
