package com.librarymanagement.LibraryManagement.author.dto;

import com.librarymanagement.LibraryManagement.ImageMetadata.ImageMetadata;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public class BaseAuthorDTO implements AuthorDTO {
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
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    private ImageMetadata imageMetadata;

    public BaseAuthorDTO() {
    }

    public BaseAuthorDTO(String firstName, String lastName, String nationality, LocalDate dateOfBirth, ImageMetadata imageMetadata) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
        this.dateOfBirth = dateOfBirth;
        this.imageMetadata = imageMetadata;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    @Override
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
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
}
