package com.librarymanagement.LibraryManagement.author.dto;

import com.librarymanagement.LibraryManagement.ImageMetadata.ImageMetadata;
import com.librarymanagement.LibraryManagement.book.dto.BaseBookDTO;
import com.librarymanagement.LibraryManagement.book.dto.BookDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.List;

public class FullAuthorDTO implements AuthorDTO {

    private long id;

    @NotNull(message = "First name cannot be null")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]{1,45}$", message = "First name must be between 1 and 45 characters and contain only letters and spaces")
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]{1,45}$", message = "Last name must be between 1 and 45 characters and contain only letters and spaces")
    private String lastName;

    @NotNull(message = "Nationality cannot be null")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]{1,45}$", message = "Nationality must be between 1 and 45 characters and contain only letters and spaces")
    private String nationality;

    @NotNull(message = "Date of birth cannot be null")
    @Past(message = "Date of birth must be in past")
    private LocalDate dateOfBirth;

    private ImageMetadata imageMetadata;

    private List<BaseBookDTO> books;

    public FullAuthorDTO() {
    }

    public FullAuthorDTO(String firstName, String lastName, LocalDate dateOfBirth, String nationality, List<BaseBookDTO> books, ImageMetadata imageMetadata) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.books = books;
        this.imageMetadata = imageMetadata;
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

    public void setBooks(List<BaseBookDTO> books) {
        this.books = books;
    }

    public List<BaseBookDTO> getBooks() {
        return books;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public ImageMetadata getImageMetadata() {
        return imageMetadata;
    }

    public void setImageMetadata(ImageMetadata imageMetadata) {
        this.imageMetadata = imageMetadata;
    }

    @Override
    public String toString() {
        return "FullAuthorDTO{" +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", nationality='" + nationality + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
