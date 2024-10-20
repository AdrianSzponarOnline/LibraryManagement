package com.librarymanagement.LibraryManagement.author;

import com.librarymanagement.LibraryManagement.author.dto.AuthorMapper;
import com.librarymanagement.LibraryManagement.author.dto.BaseAuthorDTO;
import com.librarymanagement.LibraryManagement.author.exception.AuthorNotFoundException;
import com.librarymanagement.LibraryManagement.book.Book;
import com.librarymanagement.LibraryManagement.book.BookRepository;
import com.librarymanagement.LibraryManagement.book.BookService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookService bookService;
    private final BookRepository bookRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository, BookService bookService, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookService = bookService;
        this.bookRepository = bookRepository;
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
    @Transactional
    public Book removeAuthorFromBook(long bookId, long authorId) {
        Book book = bookService.findBookOrThrow(bookId);
        Author author = findAuthorOrThrow(authorId);

        if (!book.getAuthors().contains(author)) {
            throw new IllegalArgumentException("Author with id " + authorId + " is not associated with book id " + bookId);
        }

        book.removeAuthor(author);
        author.removeBook(book);

        return bookRepository.save(book);
    }

    // helper method
    @Transactional
    public void deleteAuthorByIdFromBooks(long id) {
        authorRepository.deleteAuthorByIdFromBooks(id);
    }

    // helper methods
    private Author findAuthorOrThrow(long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author with id " + id + " does not exist"));
    }
    @Transactional
    public Book addAuthorToBook(long bookId, Author author) {
        // Znajdź książkę lub rzuć wyjątek, jeśli nie istnieje
        Book book = bookService.findBookOrThrow(bookId);

        // Sprawdzenie, czy książka już zawiera tego autora
        if (book.getAuthors().stream().anyMatch(existingAuthor ->
                existingAuthor.getFirstName().equals(author.getFirstName()) &&
                        existingAuthor.getLastName().equals(author.getLastName()) &&
                        existingAuthor.getNationality().equals(author.getNationality()) &&
                        existingAuthor.getDateOfBirth().equals(author.getDateOfBirth())
        )) {
            throw new IllegalArgumentException("Book with id " + bookId + " already contains this author");
        }

        // Szukanie autora w bazie danych
        Optional<Author> authorInDb = authorRepository.findByAuthorInfo(
                author.getFirstName(),
                author.getLastName(),
                author.getNationality(),
                author.getDateOfBirth()
        );

        if (authorInDb.isPresent()) {
            // Jeśli autor istnieje, dodaj go do książki
            Author existingAuthor = authorInDb.get();
            book.getAuthors().add(existingAuthor);
            // Zaktualizowanie relacji dwukierunkowej
            existingAuthor.getBooks().add(book);
        } else {
            // Jeśli autor nie istnieje, zapisz nowego autora
            saveAuthor(author);
            book.getAuthors().add(author);
            author.getBooks().add(book);
        }

        // Zapisz zaktualizowaną książkę i zwróć wynik
        return bookRepository.save(book);
    }

}
