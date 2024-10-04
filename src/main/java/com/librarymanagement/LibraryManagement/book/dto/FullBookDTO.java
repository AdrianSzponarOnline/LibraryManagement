package com.librarymanagement.LibraryManagement.book.dto;

import com.librarymanagement.LibraryManagement.author.dto.BaseAuthorDTO;
import com.librarymanagement.LibraryManagement.category.dto.CategoryDTO;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

public class FullBookDTO implements BookDTO{
    @NotNull(message = "Title cannot be null")
    @Size(min = 1, max = 100, message = "Title must not be empty")
    private String title;

    @NotNull(message = "ISBN cannot be null")
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{6}-\\d{1}-\\d{1}", message = "ISBN must be a 13-digit number")
    private String isbn;

    @Min(value = 1450, message = "Year should be not less than 1450")
    @Max(value = 2024, message = "Year should not me more than 2024")
    private int year;

    private List<BaseAuthorDTO> authors = new ArrayList<>();
    private List<CategoryDTO> categories  = new ArrayList<>();



    public FullBookDTO(String title, String isbn, int year, List<BaseAuthorDTO> authors, List<CategoryDTO> categories) {
        this.title = title;
        this.isbn = isbn;
        this.year = year;
        this.authors = authors;
        this.categories = categories;
    }

    public FullBookDTO() {
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<BaseAuthorDTO> getAuthors() {
        return authors;
    }

    public void setAuthors(List<BaseAuthorDTO> authors) {
        this.authors = authors;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDTO> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "FullBookDTO{" +
                "title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                ", year=" + year +
                ", authors=" + authors +
                ", categories=" + categories +
                '}';
    }
}
