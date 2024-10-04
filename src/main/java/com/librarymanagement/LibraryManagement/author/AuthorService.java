package com.librarymanagement.LibraryManagement.author;

import com.librarymanagement.LibraryManagement.author.exception.AuthorNotFoundException;
import com.librarymanagement.LibraryManagement.book.Book;
import com.librarymanagement.LibraryManagement.book.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthorService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
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

    // Read
    public List<Author> getAllAuthors() {

        return authorRepository.findAll();
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

    // Update
    @Transactional
    public Author updateAuthor(long id, Author authorDetails) {
        Author existingAuthor = findAuthorOrThrow(id);

        // Logika przekształcająca puste wartości na null
        if (authorDetails.getFirstName() != null && authorDetails.getFirstName().trim().isEmpty()) {
            existingAuthor.setFirstName(null);
        } else {
            existingAuthor.setFirstName(authorDetails.getFirstName());
        }

        if (authorDetails.getLastName() != null && authorDetails.getLastName().trim().isEmpty()) {
            existingAuthor.setLastName(null);
        } else {
            existingAuthor.setLastName(authorDetails.getLastName());
        }

        if (authorDetails.getNationality() != null && authorDetails.getNationality().trim().isEmpty()) {
            existingAuthor.setNationality(null);
        } else {
            existingAuthor.setNationality(authorDetails.getNationality());
        }

        if (authorDetails.getDateOfBirth() == null || authorDetails.getDateOfBirth().toString().trim().isEmpty()) {
            existingAuthor.setDateOfBirth(null);
        } else {
            existingAuthor.setDateOfBirth(authorDetails.getDateOfBirth());
        }

        return authorRepository.save(existingAuthor);
    }

    // Delete
    @Transactional
    public void deleteAuthorById(long id) {
        Optional<Author> optionalAuthor = authorRepository.findById(id);

        if (optionalAuthor.isPresent()) {
            Author existingAuthor = optionalAuthor.get();
            bookRepository.deleteBooksByAuthorId(id);
            authorRepository.delete(existingAuthor);  // Usuń autora
            authorRepository.flush();  // Wymuś zapisanie zmian w bazie danych
        } else {
            throw new EntityNotFoundException("Author with id " + id + " not found");
        }
    }

    // helper methods
    private Author findAuthorOrThrow(long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author with id " + id + " does not exist"));
    }
}
