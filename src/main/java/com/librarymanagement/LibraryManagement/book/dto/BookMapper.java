package com.librarymanagement.LibraryManagement.book.dto;

import com.librarymanagement.LibraryManagement.author.Author;
import com.librarymanagement.LibraryManagement.author.dto.AuthorDTO;
import com.librarymanagement.LibraryManagement.book.Book;
import com.librarymanagement.LibraryManagement.category.Category;
import com.librarymanagement.LibraryManagement.category.dto.CategoryDTO;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class BookMapper {
    public static BookDTO toDto(Book book) {
        FullBookDTO dto = new FullBookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setYear(book.getYear());

        // Map authors

        List<AuthorDTO> authors = book.getAuthors().stream()
                .map(a -> {
                    AuthorDTO authorDTO = new AuthorDTO();
                    authorDTO.setId(a.getId());
                    authorDTO.setFirstName(a.getFirstName());
                    authorDTO.setLastName(a.getLastName());
                    authorDTO.setNationality(a.getNationality());
                    authorDTO.setDateOfBirth(a.getDateOfBirth());
                    return authorDTO;
                })
                .collect(Collectors.toList());

        dto.setAuthors(authors);

        List<CategoryDTO> categories = book.getCategories().stream()
                .map(c -> {
                    CategoryDTO categoryDTO = new CategoryDTO();
                    categoryDTO.setId(c.getId());
                    categoryDTO.setCategoryName(c.getCategoryName());
                    return categoryDTO;
                })
                .collect(Collectors.toList());

        dto.setCategories(categories);

        return dto;
    }
    public static Book toEntity(BookDTO dto) {
        Book book = new Book();
        book.setId(dto.getId());
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setYear(dto.getYear());

        // Map authors

        Set<Author> authors = dto.getAuthors().stream()
                .map(a -> {
                    Author author = new Author();
                    author.setId(a.getId());
                    author.setFirstName(a.getFirstName());
                    author.setLastName(a.getLastName());
                    author.setNationality(a.getNationality());
                    author.setDateOfBirth(a.getDateOfBirth());
                    return author;
                })
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator
                        .comparing(Author::getFirstName)
                        .thenComparing(Author::getLastName))));
        book.setAuthors(authors);

        // Map categories
        Set<Category> categories = dto.getCategories().stream()
                .map(c -> {
                    Category category = new Category();
                    category.setId(c.getId());
                    category.setCategoryName(c.getCategoryName());
                    return category;
                })
                .collect(Collectors.toCollection(TreeSet::new));
        book.setCategories(categories);

        return book;
    }
}
