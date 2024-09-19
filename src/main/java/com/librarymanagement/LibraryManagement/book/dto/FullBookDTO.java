package com.librarymanagement.LibraryManagement.book.dto;

import com.librarymanagement.LibraryManagement.author.Author;
import com.librarymanagement.LibraryManagement.author.dto.AuthorDTO;
import com.librarymanagement.LibraryManagement.category.Category;
import com.librarymanagement.LibraryManagement.category.dto.CategoryDTO;

import java.util.List;

public class FullBookDTO implements BookDTO{
    private long id;
    private String title;
    private String isbn;
    private int year;
    private List<AuthorDTO> authors;
    private List<CategoryDTO> categories;

    public FullBookDTO(String title, String isbn, int year, List<AuthorDTO> authors, List<CategoryDTO> categories) {
        this.title = title;
        this.isbn = isbn;
        this.year = year;
        this.authors = authors;
        this.categories = categories;
    }

    public FullBookDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public List<AuthorDTO> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AuthorDTO> authors) {
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
        return "FullBookDTO{" + "id=" + id + ", title='" + title + '\'' + ", isbn='" + isbn + '\'' + ", year=" + year + ", authors=" + authors + ", categories=" + categories + '}';
    }
}
