package com.librarymanagement.LibraryManagement.book;

import com.librarymanagement.LibraryManagement.author.Author;
import com.librarymanagement.LibraryManagement.author.dto.AuthorMapper;
import com.librarymanagement.LibraryManagement.author.dto.BaseAuthorDTO;
import com.librarymanagement.LibraryManagement.book.dto.BookDTO;
import com.librarymanagement.LibraryManagement.book.dto.BookMapper;
import com.librarymanagement.LibraryManagement.book.dto.FullBookDTO;
import com.librarymanagement.LibraryManagement.category.Category;
import com.librarymanagement.LibraryManagement.category.dto.CategoryDTO;
import com.librarymanagement.LibraryManagement.category.dto.CategoryMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/books")
    public ResponseEntity<Page<BookDTO>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = Sort.by(sortBy);
        sort = sortDir.equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<BookDTO> books = bookService.getAllBooks(pageable).map(BookMapper::toDto); //mapping books to dto

        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable long id) {
        BookDTO bookDTO = BookMapper.toDto(bookService.getBookById(id));
        return ResponseEntity.ok(bookDTO);
    }


    @PostMapping(value = "/books")
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody final FullBookDTO bookDTO) {
        Book createdBook = bookService.saveBook(BookMapper.toEntity(bookDTO));
        BookDTO createdBookDTO = BookMapper.toDto(createdBook);
        return ResponseEntity
                .created(URI.create("/api/books/" + createdBook.getId()))
                .body(createdBookDTO);
    }

    @PostMapping(value = "/books/{bookId}/authors")
    public ResponseEntity<BookDTO> addAuthorToBook(@PathVariable long bookId, @Valid @RequestBody final BaseAuthorDTO authorDTO) {
        // Mapowanie BaseAuthorDTO do encji Author
        Author author = AuthorMapper.toEntity(authorDTO);

        // Dodanie autora do książki
        Book toUpdate = bookService.addAuthorToBook(bookId, author);

        if (toUpdate == null) {
            return ResponseEntity.notFound().build();
        }
        // Zwrócenie zaktualizowanej książki jako DTO
        return ResponseEntity.ok(BookMapper.toDto(toUpdate));
    }

    @PostMapping(value = "/books/{bookId}/categories")
    public ResponseEntity<BookDTO> addCategoryToBook(@PathVariable long bookId, @Valid @RequestBody final CategoryDTO categoryDTO) {
        Category category = CategoryMapper.toCategory(categoryDTO);
        Book toUpdate = bookService.addCategoryToBook(bookId, category);
        if (toUpdate == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(BookMapper.toDto(toUpdate));
    }

    @PutMapping(value = "/books/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable final long id, @Valid @RequestBody final FullBookDTO bookDTO) {
        Book updatedBook = bookService.updateBook(id, BookMapper.toEntity(bookDTO));
        if (updatedBook == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(BookMapper.toDto(updatedBook));
    }


    @DeleteMapping(value = "/books/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable final long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/books/{bookId}/authors/{authorId}")
    public ResponseEntity<Void> deleteAuthorFromBook(@PathVariable long bookId,
                                                     @PathVariable long authorId) {
        bookService.removeAuthorFromBook(bookId, authorId);
        return ResponseEntity.noContent().build();
    }

}
