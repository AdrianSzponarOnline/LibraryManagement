package com.librarymanagement.LibraryManagement.author.dto;

import com.librarymanagement.LibraryManagement.author.Author;
import com.librarymanagement.LibraryManagement.book.Book;
import com.librarymanagement.LibraryManagement.book.dto.BaseBookDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import static org.junit.jupiter.api.Assertions.*;

class AuthorMapperTest {

    @Test
    public void testToDTO_WithBooks() {
        // Arrange: przygotowanie danych testowych
        Author author = new Author();
        author.setFirstName("Jan");
        author.setLastName("Kowalski");
        author.setNationality("Polska");
        author.setDateOfBirth(LocalDate.of(1980, 1, 1));

        Book book1 = new Book();
        book1.setTitle("Książka Pierwsza");
        book1.setIsbn("ISBN001");
        book1.setYear(2000);

        Book book2 = new Book();
        book2.setTitle("Książka Druga");
        book2.setIsbn("ISBN002");
        book2.setYear(2005);

        author.setBooks(new TreeSet<>(Arrays.asList(book1, book2)));

        // Act: wykonanie metody
        FullAuthorDTO fullAuthorDTO = AuthorMapper.toDTO(author);

        // Assert: sprawdzenie wyników
        assertEquals("Jan", fullAuthorDTO.getFirstName());
        assertEquals("Kowalski", fullAuthorDTO.getLastName());
        assertEquals("Polska", fullAuthorDTO.getNationality());
        assertEquals(LocalDate.of(1980, 1, 1), fullAuthorDTO.getDateOfBirth());

        assertNotNull(fullAuthorDTO.getBooks());
        assertEquals(2, fullAuthorDTO.getBooks().size());

        List<String> bookTitles = fullAuthorDTO.getBooks().stream()
                .map(BaseBookDTO::getTitle)
                .toList();

        assertTrue(bookTitles.contains("Książka Pierwsza"));
        assertTrue(bookTitles.contains("Książka Druga"));
    }

    @Test
    public void testToDTO_NoBooks() {
        // Arrange: autor bez książek
        Author author = new Author();
        author.setFirstName("Anna");
        author.setLastName("Nowak");
        author.setNationality("Polska");
        author.setDateOfBirth(LocalDate.of(1990, 5, 15));

        author.setBooks(null);

        // Act: wykonanie metody
        FullAuthorDTO fullAuthorDTO = AuthorMapper.toDTO(author);

        // Assert: sprawdzenie wyników
        assertEquals("Anna", fullAuthorDTO.getFirstName());
        assertEquals("Nowak", fullAuthorDTO.getLastName());
        assertEquals("Polska", fullAuthorDTO.getNationality());
        assertEquals(LocalDate.of(1990, 5, 15), fullAuthorDTO.getDateOfBirth());

        assertNull(fullAuthorDTO.getBooks());
    }

    @Test
    public void testToBaseDTO() {
        // Arrange: przygotowanie autora
        Author author = new Author();
        author.setFirstName("Piotr");
        author.setLastName("Wiśniewski");
        author.setNationality("Polska");
        author.setDateOfBirth(LocalDate.of(1975, 12, 31));

        // Act: wykonanie metody
        BaseAuthorDTO baseAuthorDTO = AuthorMapper.toBaseDTO(author);

        // Assert: sprawdzenie wyników
        assertEquals("Piotr", baseAuthorDTO.getFirstName());
        assertEquals("Wiśniewski", baseAuthorDTO.getLastName());
        assertEquals("Polska", baseAuthorDTO.getNationality());
        assertEquals(LocalDate.of(1975, 12, 31), baseAuthorDTO.getDateOfBirth());
    }

    @Test
    public void testToEntity_BaseAuthorDTO() {
        // Arrange: przygotowanie BaseAuthorDTO
        BaseAuthorDTO baseAuthorDTO = new BaseAuthorDTO();
        baseAuthorDTO.setFirstName("Katarzyna");
        baseAuthorDTO.setLastName("Zielińska");
        baseAuthorDTO.setNationality("Polska");
        baseAuthorDTO.setDateOfBirth(LocalDate.of(1985, 7, 20));

        // Act: wykonanie metody
        Author author = AuthorMapper.toEntity(baseAuthorDTO);

        // Assert: sprawdzenie wyników
        assertEquals("Katarzyna", author.getFirstName());
        assertEquals("Zielińska", author.getLastName());
        assertEquals("Polska", author.getNationality());
        assertEquals(LocalDate.of(1985, 7, 20), author.getDateOfBirth());
    }

    @Test
    public void testToEntity_FullAuthorDTO_WithBooks() {
        // Arrange: przygotowanie FullAuthorDTO z książkami
        BaseBookDTO bookDTO1 = new BaseBookDTO();
        bookDTO1.setTitle("Książka A");
        bookDTO1.setIsbn("ISBN100");
        bookDTO1.setYear(2010);

        BaseBookDTO bookDTO2 = new BaseBookDTO();
        bookDTO2.setTitle("Książka B");
        bookDTO2.setIsbn("ISBN200");
        bookDTO2.setYear(2015);

        List<BaseBookDTO> books = Arrays.asList(bookDTO1, bookDTO2);

        FullAuthorDTO fullAuthorDTO = new FullAuthorDTO();
        fullAuthorDTO.setFirstName("Marcin");
        fullAuthorDTO.setLastName("Szymański");
        fullAuthorDTO.setNationality("Polska");
        fullAuthorDTO.setDateOfBirth(LocalDate.of(1988, 3, 10));
        fullAuthorDTO.setBooks(books);

        // Act: wykonanie metody
        Author author = AuthorMapper.toEntity(fullAuthorDTO);

        // Assert: sprawdzenie wyników
        assertEquals("Marcin", author.getFirstName());
        assertEquals("Szymański", author.getLastName());
        assertEquals("Polska", author.getNationality());
        assertEquals(LocalDate.of(1988, 3, 10), author.getDateOfBirth());

        assertNotNull(author.getBooks());
        assertEquals(2, author.getBooks().size());

        List<String> bookTitles = author.getBooks().stream()
                .map(Book::getTitle)
                .toList();

        assertTrue(bookTitles.contains("Książka A"));
        assertTrue(bookTitles.contains("Książka B"));
    }
}
