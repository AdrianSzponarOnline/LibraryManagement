package com.librarymanagement.LibraryManagement.book;

import com.librarymanagement.LibraryManagement.author.Author;
import com.librarymanagement.LibraryManagement.book.exception.BookAlreadyExistsException;
import com.librarymanagement.LibraryManagement.book.exception.BookNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    //Create
    @Transactional
    public Book saveBook(Book book) {
        Optional<Book> existingBook = bookRepository.findBookByTitleAndAuthorsAndYear(book.getTitle(), book.getAuthors(), book.getYear());
        if (existingBook.isPresent()) {
            throw new BookAlreadyExistsException("Book already exists");
        } else
            return bookRepository.save(book);
    }

    //Read
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.orElseThrow(() -> new BookNotFoundException("Book with id: " + id + " not found"));
    }

    //Update
    public Book updateBook(long id, Book book) {
        Book toUpdate = findBookOrThrow(id);
        toUpdate.setTitle(book.getTitle());
        toUpdate.setIsbn(book.getIsbn());
        toUpdate.setAuthors(book.getAuthors());
        toUpdate.setCategories(book.getCategories());
        toUpdate.setYear(book.getYear());
        return bookRepository.save(toUpdate);
    }

    //Delete
    public void deleteBookById(long id) {
        Book toDelete = findBookOrThrow(id);
        bookRepository.delete(toDelete);
    }

    // helper methods
    private Book findBookOrThrow(long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with id " + id + " not found"));
    }



    @Transactional
    public Book addAuthorToBook(long bookId, Author author) {
        Book book = findBookOrThrow(bookId);
        book.getAuthors().add(author);
        author.getBooks().add(book);
        return bookRepository.save(book);
    }

    @Transactional
    public Book removeAuthorFromBook(long bookId, Author author) {
        Book book = findBookOrThrow(bookId);
        book.getAuthors().remove(author);
        author.getBooks().remove(book);
        return bookRepository.save(book);
    }
}
