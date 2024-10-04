package com.librarymanagement.LibraryManagement.author.dto;

import com.librarymanagement.LibraryManagement.author.Author;
import com.librarymanagement.LibraryManagement.book.Book;
import com.librarymanagement.LibraryManagement.book.dto.BaseBookDTO;
import com.librarymanagement.LibraryManagement.book.dto.BookDTO;


import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class AuthorMapper {

    public static FullAuthorDTO toDTO(Author author) {
        FullAuthorDTO fullAuthorDTO = new FullAuthorDTO();
        fullAuthorDTO.setFirstName(author.getFirstName());
        fullAuthorDTO.setLastName(author.getLastName());
        fullAuthorDTO.setNationality(author.getNationality());
        fullAuthorDTO.setDateOfBirth(author.getDateOfBirth());

        //map books
        if (author.getBooks() != null) {
            List<BookDTO> books = author.getBooks().stream()
                    .map(b -> {
                        BaseBookDTO baseBookDTO = new BaseBookDTO();
                        baseBookDTO.setTitle(b.getTitle());
                        baseBookDTO.setIsbn(b.getIsbn());
                        baseBookDTO.setYear(b.getYear());
                        return baseBookDTO;
                    })
                    .collect(Collectors.toList());

            fullAuthorDTO.setBooks(books);
        }
        return fullAuthorDTO;
    }

    public static BaseAuthorDTO toBaseDTO(Author author) {
        BaseAuthorDTO baseAuthorDTO = new BaseAuthorDTO();
        baseAuthorDTO.setFirstName(author.getFirstName());
        baseAuthorDTO.setLastName(author.getLastName());
        baseAuthorDTO.setNationality(author.getNationality());
        baseAuthorDTO.setDateOfBirth(author.getDateOfBirth());
        return baseAuthorDTO;
    }

    public static Author toEntity(BaseAuthorDTO authorDTO) {
        Author author = new Author();
        author.setFirstName(authorDTO.getFirstName());
        author.setLastName(authorDTO.getLastName());
        author.setNationality(authorDTO.getNationality());
        author.setDateOfBirth(authorDTO.getDateOfBirth());
        return author;
    }

    public static Author toEntity(FullAuthorDTO fullAuthorDTO) {
        Author author = new Author();
        author.setFirstName(fullAuthorDTO.getFirstName());
        author.setLastName(fullAuthorDTO.getLastName());
        author.setNationality(fullAuthorDTO.getNationality());
        author.setDateOfBirth(fullAuthorDTO.getDateOfBirth());

        //mapping books
        TreeSet<Book> books = fullAuthorDTO.getBooks().stream()
                .map(b -> {
                    Book book = new Book();
                    book.setTitle(b.getTitle());
                    book.setIsbn(b.getIsbn());
                    book.setYear(b.getYear());
                    return book;
                })
                .collect(Collectors.toCollection(TreeSet::new));
        author.setBooks(books);
        return author;
    }
}
