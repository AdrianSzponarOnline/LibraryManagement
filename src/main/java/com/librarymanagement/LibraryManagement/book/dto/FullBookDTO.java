package com.librarymanagement.LibraryManagement.book.dto;

import com.librarymanagement.LibraryManagement.author.Author;
import com.librarymanagement.LibraryManagement.category.Category;

import java.util.List;

public class FullBookDTO {
    private long id;
    private String title;
    private String isbn;
    private int year;
    private List<Author> authors;
    private List<Category> categories;

    public FullBookDTO(String title, String isbn, int year, List<Author> authors, List<Category> categories) {
        this.title = title;
        this.isbn = isbn;
        this.year = year;
        this.authors = authors;
        this.categories = categories;
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

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "FullBookDTO{" + "id=" + id + ", title='" + title + '\'' + ", isbn='" + isbn + '\'' + ", year=" + year + ", authors=" + authors + ", categories=" + categories + '}';
    }
}
