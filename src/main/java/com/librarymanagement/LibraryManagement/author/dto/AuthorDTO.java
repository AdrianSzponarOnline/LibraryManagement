package com.librarymanagement.LibraryManagement.author.dto;

import com.librarymanagement.LibraryManagement.book.Book;
import com.librarymanagement.LibraryManagement.book.dto.BookDTO;

import java.time.LocalDate;
import java.util.List;

public interface AuthorDTO {
    String getFirstName();
    String getLastName();
    String getNationality();
    LocalDate getDateOfBirth();
}
