package com.librarymanagement.LibraryManagement.book.dto;

import com.librarymanagement.LibraryManagement.author.Author;
import com.librarymanagement.LibraryManagement.author.dto.AuthorMapper;
import com.librarymanagement.LibraryManagement.author.dto.BaseAuthorDTO;
import com.librarymanagement.LibraryManagement.book.Book;
import com.librarymanagement.LibraryManagement.category.Category;
import com.librarymanagement.LibraryManagement.category.dto.CategoryDTO;
import com.librarymanagement.LibraryManagement.category.dto.CategoryMapper;

import java.util.*;
import java.util.stream.Collectors;

public class BookMapper {
    public static BookDTO toDto(Book book) {
        FullBookDTO dto = new FullBookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setYear(book.getYear());


        List<BaseAuthorDTO> authors = book.getAuthors().stream()
                .map(AuthorMapper::toBaseDTO)
                .collect(Collectors.toList());

        dto.setAuthors(authors);

        List<CategoryDTO> categories = book.getCategories().stream()
                .map(CategoryMapper::toCategoryDTO)
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

        // Map
        if (dto instanceof FullBookDTO fullBookDTO) {
            Set<Author> authors = fullBookDTO.getAuthors().stream()
                    .map(AuthorMapper::toEntity)
                    .collect(Collectors.toCollection(HashSet::new));
            book.setAuthors(authors);

            // Map categories
            Set<Category> categories = fullBookDTO.getCategories().stream()
                    .map(CategoryMapper::toCategory)
                    .collect(Collectors.toCollection(HashSet::new));
            book.setCategories(categories);
        }
        return book;
    }
}

