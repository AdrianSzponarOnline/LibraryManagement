package com.librarymanagement.LibraryManagement.author;

import com.librarymanagement.LibraryManagement.book.Book;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "author", uniqueConstraints = @UniqueConstraint(columnNames = {"first_name", "last_name"}))
public class Author implements Comparable<Author>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "first_name")
    @NotBlank(message = "First name cannot be empty")
    String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "Last name cannot be empty")
    String lastName;

    @Column(name = "nationality")
    String nationality;

    @Column(name = "date_of_birth")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate dateOfBirth;

    @ManyToMany(mappedBy = "authors")
    private final Set<Book> books = new TreeSet<>();

    public Author() {
    }

    public Author(String firstName, String lastName, String nationality, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
        this.dateOfBirth = dateOfBirth;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Set<Book> getBooks() {
        return books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(firstName, author.firstName)
                && Objects.equals(lastName, author.lastName)
                && Objects.equals(nationality, author.nationality)
                && Objects.equals(dateOfBirth, author.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, nationality, dateOfBirth);
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", nationality='" + nationality + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                '}';
    }

    @Override
    public int compareTo(Author o) {
        int lastNameComparison = this.lastName.compareTo(o.lastName);
        if (lastNameComparison != 0) {
            return lastNameComparison;
        }

        // Jeśli nazwiska są równe, porównujemy imiona
        int firstNameComparison = this.firstName.compareTo(o.firstName);
        if (firstNameComparison != 0) {
            return firstNameComparison;
        }

        // Jeśli nazwiska i imiona są równe, porównujemy daty urodzenia
        return this.dateOfBirth.compareTo(o.dateOfBirth);
    }
}
