package com.librarymanagement.LibraryManagement.book;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    //Create
    @Transactional
    public void saveBook(Book book) {
        Optional<Book> existingBook = bookRepository.findByTitleAndGenreIdAndYear(book.getTitle(), book.getGenreId(), book.getYear());
        if (existingBook.isPresent()) {
            throw new IllegalArgumentException("Book already exists");
        }
        else
            bookRepository.save(book);
    }

    //Read
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.orElse(null);
    }

    //Update

    //Delete

}
