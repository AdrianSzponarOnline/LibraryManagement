package com.librarymanagement.LibraryManagement.book;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(final BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/books")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping(value = "/books/{id}", produces = "application/json")
    public ResponseEntity<Book> getBookById(@PathVariable final long id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @PostMapping(value = "/books")
    public ResponseEntity<Book> createBook(@Valid @RequestBody final Book book) {
        book.setId(0);
        Book createdBook = bookService.saveBook(book);
        return ResponseEntity.
                created(URI.create("/api/books/" + createdBook.getId()))
                .body(createdBook);
    }

    @PutMapping(value = "/books/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable final long id, @Valid @RequestBody final Book book) {
        Book toUpdate = bookService.getBookById(id);
        bookService.updateBook(id, book);
        return ResponseEntity.ok(toUpdate);
    }

    @DeleteMapping(value = "books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable final long id) {
        Book toDelete = bookService.getBookById(id);
        if (toDelete == null) {
            return ResponseEntity.notFound().build();
        }
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }
}
