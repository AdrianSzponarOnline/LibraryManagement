package com.librarymanagement.LibraryManagement.book;

import com.librarymanagement.LibraryManagement.author.Author;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "book", uniqueConstraints = { @UniqueConstraint(name = "UK_book_title_genre_year", columnNames = {"title", "genre_id", "year"})})
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "genre_id")
    private long genreId;

    @Column(name = "year")
    private int year;

    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();

    public Book() {
    }

    public Book(String title, long genreId, int year) {
        this.title = title;
        this.genreId = genreId;
        this.year = year;
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

    public long getGenreId() {
        return genreId;
    }

    public void setGenreId(long genreId) {
        this.genreId = genreId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return genreId == book.genreId && year == book.year && Objects.equals(title, book.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, genreId, year);
    }

    @Override
    public String toString() {
        return "Book{" + "id=" + id + ", title='" + title + '\'' + ", genreId=" + genreId + ", year=" + year + '}';
    }
}
