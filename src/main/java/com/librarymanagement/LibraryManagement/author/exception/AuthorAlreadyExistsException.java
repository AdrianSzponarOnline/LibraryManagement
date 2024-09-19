package com.librarymanagement.LibraryManagement.author.exception;

public class AuthorAlreadyExistsException extends RuntimeException {
    public AuthorAlreadyExistsException(String message) {
        super(message);
    }
}
