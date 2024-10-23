package com.librarymanagement.LibraryManagement.author;

import com.librarymanagement.LibraryManagement.author.dto.AuthorDTO;
import com.librarymanagement.LibraryManagement.author.dto.AuthorMapper;
import com.librarymanagement.LibraryManagement.author.dto.BaseAuthorDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(final AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping(value = "/authors")
    public ResponseEntity<Set<BaseAuthorDTO>> getAllAuthors() {
        List<Author> authors = authorService.getAllAuthors();

        if (authors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Mapowanie listy Author na Set BaseAuthorDTO
        Set<BaseAuthorDTO> authorDTOS = authors.stream()
                .map(AuthorMapper::toBaseDTO)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(authorDTOS);
    }


    @GetMapping(value = "/authors/{id}", produces = "application/json")
    public ResponseEntity<Author> getAuthor(@PathVariable final long id) {
        Author author = authorService.getAuthorById(id);
        return ResponseEntity.ok(author);
    }

    @PostMapping(value = "/authors")
    public ResponseEntity<Author> createAuthor(@Valid @RequestBody AuthorDTO authorDTO) {
        Author author = AuthorMapper.toEntity((BaseAuthorDTO) authorDTO);
        author.setId(0);
        Author createdAuthor = authorService.saveAuthor(author);
        return ResponseEntity
                .created(URI.create("api/authors/" + createdAuthor.getId()))
                .body(createdAuthor);
    }

    @PutMapping(value = "/authors/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable final long id, @Valid @RequestBody final BaseAuthorDTO authorDTO) {
        Author authorToUpdate = AuthorMapper.toEntity(authorDTO);
        authorService.updateAuthor(id, authorToUpdate);
        return ResponseEntity.ok(authorToUpdate);
    }

    @DeleteMapping(value = "authors/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable final long id) {
        authorService.deleteAuthorById(id);  // Perform the deletion
        return ResponseEntity.noContent().build();  // Return 204 (No Content) on successful deletion
    }
}
