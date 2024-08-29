package com.librarymanagement.LibraryManagement.author;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping(value = "/authors")
    public ResponseEntity<List<Author>> getAllAuthors() {
        List<Author> authors = authorService.getAllAuthors();
        if (authors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(authors);
    }

    @GetMapping(value = "/authors/{id}", produces = "application/json")
    public ResponseEntity<Author> getAuthor(@PathVariable int id) {
        Author author = authorService.getAuthorById(id);
        if (author == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(author);
    }

    @PostMapping(value = "/authors")
    public ResponseEntity<Author> createAuthor(@Valid @RequestBody Author author) {
        author.setId(0);
        Author createdAuthor = authorService.saveAuthor(author);
        return ResponseEntity.ok(createdAuthor);
    }

    @PutMapping(value = "/authors/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable int id, @Valid @RequestBody Author author) {
        Author toUpdate = authorService.getAuthorById(id);
        if (toUpdate == null) {
            return ResponseEntity.notFound().build();
        }

        authorService.updateAuthor(id, author);
        return ResponseEntity.ok(author);
    }
}
