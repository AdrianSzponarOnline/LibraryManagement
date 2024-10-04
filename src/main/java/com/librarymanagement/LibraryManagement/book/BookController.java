package com.librarymanagement.LibraryManagement.book;

import com.librarymanagement.LibraryManagement.author.dto.BaseAuthorDTO;
import com.librarymanagement.LibraryManagement.book.dto.BookDTO;
import com.librarymanagement.LibraryManagement.book.dto.BookMapper;
import com.librarymanagement.LibraryManagement.book.dto.FullBookDTO;
import com.librarymanagement.LibraryManagement.category.dto.CategoryDTO;
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
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping(value = "/books/{id}", produces = "application/json")
    public ResponseEntity<BookDTO> getBookById(@PathVariable final long id) {
        BookDTO bookDTO = bookService.getBookById(id);
        return ResponseEntity.ok(bookDTO);
    }

    @PostMapping(value = "/books")
    public ResponseEntity<Book> createBook(@Valid @RequestBody final FullBookDTO bookDTO) {
        Book toCreate = bookService.saveBook(bookDTO);
        return ResponseEntity.
                created(URI.create("/api/books/" + toCreate.getId()))
                .body(toCreate);
    }
    @PostMapping(value = "/books/{bookId}/authors")
    public ResponseEntity<BookDTO> addAuthorToBook(@PathVariable long bookId, @RequestBody final BaseAuthorDTO authorDTO) {
        Book toUpdate = bookService.addAuthorsToBook(bookId, authorDTO);
        if (toUpdate == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(BookMapper.toDto(toUpdate));
    }
    @PostMapping(value = "/books/{bookId}/categories")
    public ResponseEntity<BookDTO> addCategoryToBook(@PathVariable long bookId, @RequestBody final CategoryDTO categoryDTO) {
        Book toUpdate = bookService.addCategoryToBook(bookId, categoryDTO);
        if(toUpdate == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(BookMapper.toDto(toUpdate));
    }

    @PutMapping(value = "/books/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable final long id, @Valid @RequestBody final FullBookDTO bookDTO) {
        BookDTO toUpdate = bookService.getBookById(id);
        bookService.updateBook(id, bookDTO);
        return ResponseEntity.ok(toUpdate);
    }


    @DeleteMapping(value = "books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable final long id) {
        BookDTO toDelete = bookService.getBookById(id);
        if (toDelete == null) {
            return ResponseEntity.notFound().build();
        }
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }
}
