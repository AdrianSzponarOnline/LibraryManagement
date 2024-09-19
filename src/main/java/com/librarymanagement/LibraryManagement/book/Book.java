package com.librarymanagement.LibraryManagement.book;

import com.librarymanagement.LibraryManagement.author.Author;
import com.librarymanagement.LibraryManagement.category.Category;
import jakarta.persistence.*;


import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "book", uniqueConstraints = {@UniqueConstraint(name = "UK_isbn", columnNames = {"isbn"})})
public class Book implements Comparable<Book> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "year")
    private int year;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new TreeSet<>();

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "book_category", //tabela pośrednicząca
            joinColumns = @JoinColumn(name = "book_id"), //kolumna powiązana z encją Book
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new TreeSet<>(Comparator.comparing(Category::getCategoryName));

    public Book() {
    }

    public Book( String title, String isbn, Set<Category> categories, int year, Set<Author> authors) {
        this.title = title;
        this.isbn = isbn;
        this.categories = categories;
        this.year = year;
        this.authors = authors;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public TreeSet<Author> getAuthors() {
        return new TreeSet<>(authors);
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return "Book{" + "id=" + id + ", title='" + title + '\'' + ", categories=" + categories + ", year=" + year + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn) ||
                (Objects.equals(title, book.title) && Objects.equals(authors, book.authors) && year == book.year);
    }

    @Override
    public int hashCode() {
        return isbn != null ? Objects.hash(isbn) : Objects.hash(title, authors, year);
    }


    @Override
    public int compareTo(Book other) {
        return this.title.compareTo(other.title);
    }
}
