package com.librarymanagement.LibraryManagement.book.dto;

import jakarta.validation.constraints.*;

public class BaseBookDTO implements BookDTO{
    @NotNull(message = "Title cannot be null")
    @Size(min = 1, max = 100, message = "Title must not be empty")
    private String title;

    @NotNull(message = "ISBN cannot be null")
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{6}-\\d{1}-\\d{1}", message = "ISBN must be a 13-digit number")
    private String isbn;

    @Min(value = 1450, message = "Year should be not less than 1450")
    @Max(value = 2024, message = "Year should not me more than 2024")
    private int year;



    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getIsbn() {
        return isbn;
    }

    @Override
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public void setYear(int year) {
        this.year = year;
    }

    public BaseBookDTO() {
    }

    public BaseBookDTO(long id, String title, String isbn, int year) {
        this.title = title;
        this.isbn = isbn;
        this.year = year;
    }
}
