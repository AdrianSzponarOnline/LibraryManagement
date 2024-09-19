package com.librarymanagement.LibraryManagement.category;

import com.librarymanagement.LibraryManagement.book.Book;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String categoryName;

    @ManyToMany(mappedBy = "categories")
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
                ", books=" + books +
                '}';
    }
}
