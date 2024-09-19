package com.librarymanagement.LibraryManagement.author;

import com.librarymanagement.LibraryManagement.author.exception.AuthorNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        Optional<Author> existingAuthor = authorRepository.findByFirstNameAndLastName(author.getFirstName(), author.getLastName());

        if (existingAuthor.isPresent()) {
            throw new IllegalArgumentException("Provided Author already exists");
        }
        return authorRepository.save(author);
    }
    // Read
    public List<Author> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();

        return authors;
    }

    public Author getAuthorById(long id) {
        Optional<Author> author = authorRepository.findById(id);
        return author.orElseThrow(() -> new AuthorNotFoundException("Author with id " + id + " does not exist"));
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
        Author existingAuthor = findAuthorOrThrow(id);
        authorRepository.delete(existingAuthor);
    }

    // helper methods
    private Author findAuthorOrThrow(long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author with id " + id + " does not exist"));
    }
}
