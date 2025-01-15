package com.librarymanagement.LibraryManagement.author;

import com.librarymanagement.LibraryManagement.ImageMetadata.ImageMetadata;
import com.librarymanagement.LibraryManagement.ImageMetadata.ImageMetadataService;
import com.librarymanagement.LibraryManagement.author.dto.AuthorMapper;
import com.librarymanagement.LibraryManagement.author.dto.BaseAuthorDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;


@RestController
@RequestMapping("/api")
public class AuthorController {

    private final AuthorService authorService;
    private final ImageMetadataService imageMetadataService;

    @Autowired
    public AuthorController(final AuthorService authorService, final ImageMetadataService imageMetadataService) {
        this.authorService = authorService;
        this.imageMetadataService = imageMetadataService;
    }

    @GetMapping(value = "/authors")
    public ResponseEntity<Page<BaseAuthorDTO>> getAllAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BaseAuthorDTO> authors = authorService.getAllAuthors(pageable).map(AuthorMapper::toBaseDTO);

        if (authors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(authors);
    }
    @PostMapping(value = "/authors/{authorId}/profile-image")
    public ResponseEntity<String> uploadProfileImage(@PathVariable("authorId") int authorId,
                                                     @RequestParam("image") MultipartFile image,
                                                     @RequestParam(value = "caption", required = false) String caption) {
    try{
        Author author = authorService.getAuthorById(authorId);
        ImageMetadata imageMetadata = imageMetadataService.uploadImageWithCaption(image, caption);
        author.setImageMetadata(imageMetadata);
        authorService.saveAuthor(author);
        return ResponseEntity.created(URI.create("/api/authors/" + authorId + "/profile-image")).build();
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to upload image with id " + authorId + ": " + e.getMessage());
        }
    }




    @GetMapping(value = "/authors/{id}", produces = "application/json")
    public ResponseEntity<Author> getAuthor(@PathVariable final long id) {
        Author author = authorService.getAuthorById(id);
        return ResponseEntity.ok(author);
    }

    @PostMapping(value = "/authors")
    public ResponseEntity<Author> createAuthor(@Valid @RequestBody BaseAuthorDTO authorDTO) {
        Author author = AuthorMapper.toEntity(authorDTO);
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
