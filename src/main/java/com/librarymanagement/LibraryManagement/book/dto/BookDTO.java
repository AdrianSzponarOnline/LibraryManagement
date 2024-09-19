package com.librarymanagement.LibraryManagement.book.dto;

import com.librarymanagement.LibraryManagement.author.dto.AuthorDTO;
import com.librarymanagement.LibraryManagement.category.dto.CategoryDTO;

import java.util.List;

public interface BookDTO {
    long getId();
    String getTitle();
    String getIsbn();
    int getYear();
    List<AuthorDTO> getAuthors();
    List<CategoryDTO> getCategories();
}
