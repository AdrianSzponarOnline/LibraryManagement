package com.librarymanagement.LibraryManagement.book.dto;

import com.librarymanagement.LibraryManagement.author.Author;
import com.librarymanagement.LibraryManagement.author.dto.AuthorMapper;
import com.librarymanagement.LibraryManagement.author.dto.BaseAuthorDTO;
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
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setYear(book.getYear());


        List<BaseAuthorDTO> authors = book.getAuthors().stream()
                .map(AuthorMapper::toBaseDTO)
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
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setYear(dto.getYear());

        // Map authors
        if (dto instanceof FullBookDTO fullBookDTO) {
            Set<Author> authors = fullBookDTO.getAuthors().stream()
                    .map(AuthorMapper::toEntity)
                    .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator
                            .comparing(Author::getFirstName)
                            .thenComparing(Author::getLastName))));
            book.setAuthors(authors);

            // Map categories
            Set<Category> categories = fullBookDTO.getCategories().stream()
                    .map(c -> {
                        Category category = new Category();
                        category.setId(c.getId());
                        category.setCategoryName(c.getCategoryName());
                        return category;
                    })
                    .collect(Collectors.toCollection(TreeSet::new));
            book.setCategories(categories);
        }
        return book;
    }
}
