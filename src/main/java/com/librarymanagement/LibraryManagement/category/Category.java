package com.librarymanagement.LibraryManagement.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.librarymanagement.LibraryManagement.book.Book;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "category")
public class Category implements Comparable<Category> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String categoryName;

    @ManyToMany(mappedBy = "categories")
    @JsonIgnore
    private Set<Book> books = new TreeSet<>();

    public Category() {
    }

    public Category(String name, Set<Book> books) {
        this.categoryName = name;
        this.books = books;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String name) {
        this.categoryName = name;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(categoryName, category.categoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(categoryName);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + categoryName + '\'' +
                '}';
    }

    @Override
    public int compareTo(Category o) {
        return this.categoryName.compareTo(o.categoryName);
    }

    //helper methods

    public void addBook(Book book) {
        this.books.add(book);
        book.getCategories().add(this);
    }

    public void removeBook(Book book) {
        books.remove(book);
        book.getCategories().remove(this);
    }
}
