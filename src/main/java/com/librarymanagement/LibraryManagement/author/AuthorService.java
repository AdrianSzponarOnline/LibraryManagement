package com.librarymanagement.LibraryManagement.author;
import com.librarymanagement.LibraryManagement.author.exception.AuthorNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    // Create
    @Transactional
    public Author saveAuthor(Author author) {
        // Szukanie autora po imieniu, nazwisku oraz innych atrybutach
        Optional<Author> existingAuthor = authorRepository.findAuthorByFirstNameAndLastNameAndNationalityAndDateOfBirth(
                author.getFirstName(),
                author.getLastName(),
                author.getNationality(),
                author.getDateOfBirth()
        );

        if (existingAuthor.isPresent()) {
            throw new IllegalArgumentException("Provided Author already exists");
        }

        return authorRepository.save(author);
    }

    public HashSet<Author> saveAuthors(Set<Author> authors) {
        HashSet<Author> savedAuthors = new HashSet<>();

        // Pobierz wszystkich istniejących autorów na podstawie kluczowych informacji
        Set<Author> existingAuthors = new HashSet<>(authorRepository.findAllByAuthorInfo(
                authors.stream().map(Author::getFirstName).collect(Collectors.toSet()),
                authors.stream().map(Author::getLastName).collect(Collectors.toSet()),
                authors.stream().map(Author::getNationality).collect(Collectors.toSet()),
                authors.stream().map(Author::getDateOfBirth).collect(Collectors.toSet())
        ));

        // Sprawdź autorów, których jeszcze nie ma w bazie
        for (Author author : authors) {
            boolean exists = existingAuthors.stream().anyMatch(existingAuthor ->
                    existingAuthor.getFirstName().equals(author.getFirstName()) &&
                            existingAuthor.getLastName().equals(author.getLastName()) &&
                            existingAuthor.getNationality().equals(author.getNationality()) &&
                            existingAuthor.getDateOfBirth().equals(author.getDateOfBirth())
            );

            // Jeśli autor nie istnieje, zapisujemy go
            if (!exists) {
                Author savedAuthor = authorRepository.save(author);
                savedAuthors.add(savedAuthor);
            }
        }

        return savedAuthors;
    }

    // Read
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }
    public Page<Author> getAllAuthors(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    public Author getAuthorById(long id) {
        Optional<Author> author = authorRepository.findById(id);
        return author.orElseThrow(() -> new AuthorNotFoundException("Author with id " + id + " does not exist"));
    }

    public Optional<Author> getAuthorByFirstNameAndLastNameAndNationalityAndDateOfBirth(Author author) {
        return authorRepository.findAuthorByFirstNameAndLastNameAndNationalityAndDateOfBirth(
                author.getFirstName(),
                author.getLastName(),
                author.getNationality(),
                author.getDateOfBirth()
        );
    }
    public Optional<Author> getAuthorByAuthorInfo(String firstName, String lastName, String nationality, LocalDate dateOfBirth) {
        return authorRepository.findByAuthorInfo(firstName, lastName, nationality, dateOfBirth);
    }

    // Update
    @Transactional
    public Author updateAuthor(long id, Author authorDetails) {
        Author existingAuthor = findAuthorOrThrow(id);
        existingAuthor.setFirstName(authorDetails.getFirstName());
        existingAuthor.setLastName(authorDetails.getLastName());
        existingAuthor.setNationality(authorDetails.getNationality());
        existingAuthor.setDateOfBirth(authorDetails.getDateOfBirth());

        return authorRepository.save(existingAuthor);
    }

    // Delete
    @Transactional
    public void deleteAuthorById(long id) {
        // find author in db
        Optional<Author> optionalAuthor = authorRepository.findById(id);
        // if author exists
        if (optionalAuthor.isPresent()) {
            Author existingAuthor = optionalAuthor.get();
            deleteAuthorByIdFromBooks(existingAuthor.getId());
            authorRepository.delete(existingAuthor);
        } else {
            throw new EntityNotFoundException("Author with id " + id + " not found");
        }
    }



    // helper method
    @Transactional
    public void deleteAuthorByIdFromBooks(long id) {
        authorRepository.deleteAuthorByIdFromBooks(id);
    }

    // helper methods
    public Author findAuthorOrThrow(long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author with id " + id + " does not exist"));
    }
}
