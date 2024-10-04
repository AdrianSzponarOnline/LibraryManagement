package com.librarymanagement.LibraryManagement.book.dto;


import java.util.List;

public interface BookDTO {
    String getTitle();
    String getIsbn();
    int getYear();

    void setTitle(String title);
    void setIsbn(String isbn);
    void setYear(int year);
}
