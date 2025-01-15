package com.librarymanagement.LibraryManagement.author.dto;

import com.librarymanagement.LibraryManagement.author.Author;
import com.librarymanagement.LibraryManagement.book.Book;
import com.librarymanagement.LibraryManagement.book.dto.BaseBookDTO;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class AuthorMapper {

    public static FullAuthorDTO toDTO(Author author) {
        FullAuthorDTO fullAuthorDTO = new FullAuthorDTO();
        fullAuthorDTO.setId(author.getId());
        fullAuthorDTO.setFirstName(author.getFirstName());
        fullAuthorDTO.setLastName(author.getLastName());
        fullAuthorDTO.setNationality(author.getNationality());
        fullAuthorDTO.setDateOfBirth(author.getDateOfBirth());
        fullAuthorDTO.setImageMetadata(author.getImageMetadata());

        //map books
        if (author.getBooks() != null) {
            List<BaseBookDTO> books = author.getBooks().stream()
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
        baseAuthorDTO.setId(author.getId());
        baseAuthorDTO.setFirstName(author.getFirstName());
        baseAuthorDTO.setLastName(author.getLastName());
        baseAuthorDTO.setNationality(author.getNationality());
        baseAuthorDTO.setDateOfBirth(author.getDateOfBirth());
        baseAuthorDTO.setImageMetadata(author.getImageMetadata());
        return baseAuthorDTO;
    }

    public static Author toEntity(BaseAuthorDTO authorDTO) {
        Author author = new Author();
        author.setId(authorDTO.getId());
        author.setFirstName(authorDTO.getFirstName());
        author.setLastName(authorDTO.getLastName());
        author.setNationality(authorDTO.getNationality());
        author.setDateOfBirth(authorDTO.getDateOfBirth());
        author.setImageMetadata(authorDTO.getImageMetadata());
        return author;
    }

    public static Author toEntity(FullAuthorDTO fullAuthorDTO) {
        Author author = new Author();
        author.setId(fullAuthorDTO.getId());
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
