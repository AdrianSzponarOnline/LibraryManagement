package com.librarymanagement.LibraryManagement.author.dto;
import java.time.LocalDate;

public interface AuthorDTO {
    String getFirstName();
    String getLastName();
    String getNationality();
    LocalDate getDateOfBirth();
}
