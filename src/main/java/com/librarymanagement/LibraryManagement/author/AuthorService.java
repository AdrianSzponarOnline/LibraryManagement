package com.librarymanagement.LibraryManagement.author;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {
    AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    // Create
    public void saveAuthor(Author author) {
        Optional<Author> existingAuthor =
                authorRepository.findByFirstNameAndLastName(author.getFirstName(), author.getLastName());

        if (existingAuthor.isPresent()) {
            throw new IllegalArgumentException("Provided Author already exists");
        }
        authorRepository.save(author);
    }

    // Read
    public List<Author> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        return authors;
    }

    public Author getAuthorById(long id) {
        Optional<Author> author = authorRepository.findById(id);
        return author.orElse(null);
    }

    // Update
    public Author updateAuthor(long id, Author author) {
        Optional<Author> existinAuthorOptional = authorRepository.findById(author.getId());

        if (existinAuthorOptional.isPresent()) {
            Author existingAuthor = existinAuthorOptional.get();
            existingAuthor.setFirstName(author.getFirstName());
            existingAuthor.setLastName(author.getLastName());
            existingAuthor.setNationality(author.getNationality());
            existingAuthor.setDateOfBirth(author.getDateOfBirth());
            return authorRepository.save(existingAuthor);
        }else
            throw new IllegalArgumentException("Author with id " + id + " does not exist");
    }

    // Delete
    public void deleteAuthorById(long id) {
        if (authorRepository.existsById(id)) {
            authorRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Provided Author does not exist");
        }
    }
}
