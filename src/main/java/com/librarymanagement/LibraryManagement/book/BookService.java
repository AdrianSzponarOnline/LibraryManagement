package com.librarymanagement.LibraryManagement.book;

import com.librarymanagement.LibraryManagement.author.Author;
import com.librarymanagement.LibraryManagement.author.AuthorService;
import com.librarymanagement.LibraryManagement.book.dto.BookDTO;
import com.librarymanagement.LibraryManagement.book.exception.BookAlreadyExistsException;
import com.librarymanagement.LibraryManagement.book.exception.BookNotFoundException;
import com.librarymanagement.LibraryManagement.category.Category;
import com.librarymanagement.LibraryManagement.category.CategoryService;
import com.librarymanagement.LibraryManagement.category.dto.CategoryDTO;
import com.librarymanagement.LibraryManagement.category.dto.CategoryMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


import static com.librarymanagement.LibraryManagement.book.dto.BookMapper.toDto;


@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorService authorService, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    @Transactional
    public Book saveBook(Book toSave) throws BookAlreadyExistsException {
        // Process authors.html and categories if present, otherwise assign empty sets
        Set<Author> authors = new HashSet<>();
        Set<Category> categories = new HashSet<>();

        if (toSave.getAuthors() != null && !toSave.getAuthors().isEmpty()) {
            authors = processAuthors(toSave.getAuthors(), toSave);
        }

        if (toSave.getCategories() != null && !toSave.getCategories().isEmpty()) {
            categories = processCategories(toSave.getCategories(), toSave);
        }

        // Set authors.html and categories to the book
        toSave.setAuthors(authors);
        toSave.setCategories(categories);

        // Now, check if the book already exists
        checkIfBookExists(toSave);

        // Save the book to persist the relationships
        return bookRepository.save(toSave);
    }


    //Read

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public BookDTO getBookById(long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            return toDto(book.get());
        } else {
            throw new BookNotFoundException("Book not found");
        }
    }


    @Transactional
    public Book updateBook(long id, Book toUpdate) {
        Book inDb = findBookOrThrow(id);

        // Aktualizacja podstawowych pól
        inDb.setTitle(toUpdate.getTitle());
        inDb.setIsbn(toUpdate.getIsbn());
        inDb.setYear(toUpdate.getYear());

        // Aktualizacja autorów
        if (toUpdate.getAuthors() != null && !toUpdate.getAuthors().isEmpty()) {
            Set<Author> updatedAuthors = processAuthors(toUpdate.getAuthors(), inDb);
            inDb.setAuthors(updatedAuthors);  // Aktualizuj autorów w encji z bazy (inDb)
        }

        // Aktualizacja kategorii
        if (toUpdate.getCategories() != null && !toUpdate.getCategories().isEmpty()) {
            Set<Category> updatedCategories = processCategories(new HashSet<>(toUpdate.getCategories()), inDb);
            inDb.setCategories(updatedCategories);  // Aktualizuj kategorie w encji z bazy (inDb)
        }

        // Zapisz zaktualizowaną książkę
        return bookRepository.save(inDb);  // Zapisujemy encję pobraną z bazy, a nie toUpdate
    }


    //Delete
    public boolean deleteBookById(long id) {
        boolean isDeleted = false;
        Book toDelete = findBookOrThrow(id);
        if (toDelete != null) {
            bookRepository.delete(toDelete);
            isDeleted = true;
        }
        return isDeleted;
    }

    // helper methods
    public Book findBookOrThrow(long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with id " + id + " not found"));
    }

    @Transactional
    public Book addAuthorToBook(long bookId, Author author) {
        // Znajdź książkę lub rzuć wyjątek, jeśli nie istnieje
        Book book = findBookOrThrow(bookId);

        // Sprawdzenie, czy książka już zawiera tego autora
        if (book.getAuthors().stream().anyMatch(existingAuthor ->
                existingAuthor.getFirstName().equals(author.getFirstName()) &&
                        existingAuthor.getLastName().equals(author.getLastName()) &&
                        existingAuthor.getNationality().equals(author.getNationality()) &&
                        existingAuthor.getDateOfBirth().equals(author.getDateOfBirth())
        )) {
            throw new IllegalArgumentException("Book with id " + bookId + " already contains this author");
        }


        // Szukanie autora w bazie danych
        Optional<Author> authorInDb = authorService.getAuthorByAuthorInfo(author
        );

        if (authorInDb.isPresent()) {
            // Jeśli autor istnieje, dodaj go do książki
            Author existingAuthor = authorInDb.get();
            book.getAuthors().add(existingAuthor);
            // Zaktualizowanie relacji dwukierunkowej
            existingAuthor.getBooks().add(book);
        } else {
            // Jeśli autor nie istnieje, zapisz nowego autora
            authorService.saveAuthor(author);
            book.getAuthors().add(author);
            author.getBooks().add(book);
        }

        // Zapisz zaktualizowaną książkę i zwróć wynik
        return bookRepository.save(book);
    }
    @Transactional
    public Book removeAuthorFromBook(long bookId, long authorId) {
        Book book = findBookOrThrow(bookId);
        Author author = authorService.findAuthorOrThrow(authorId);

        if (!book.getAuthors().contains(author)) {
            throw new IllegalArgumentException("Author with id " + authorId + " is not associated with book id " + bookId);
        }

        book.removeAuthor(author);
        author.removeBook(book);

        return bookRepository.save(book);
    }

    @Transactional
    public Book addCategoryToBook(long bookId, CategoryDTO categoryToAdd) {
        Book book = findBookOrThrow(bookId);
        Category newCategory = CategoryMapper.toCategory(categoryToAdd);
        categoryService.getByName(categoryToAdd.getCategoryName());

        book.addCategory(newCategory);
        return bookRepository.save(book);
    }

    private void checkIfBookExists(Book book) {
        Optional<Book> existingBook = bookRepository.findBookByTitleAndAuthorsAndYear(
                book.getTitle(), book.getAuthors(), book.getYear()
        );
        if (existingBook.isPresent()) {
            throw new BookAlreadyExistsException("Book already exists");
        }
    }


    @Transactional
    protected Set<Category> processCategories(Set<Category> categoryDTOs, Book book) {
        Set<Category> processedCategories = new HashSet<>();

        for (Category categoryToProcess : categoryDTOs) {
            // Sprawdź, czy kategoria o danej nazwie już istnieje w bazie danych
            Optional<Category> existingCategory = categoryService.getByName(categoryToProcess.getCategoryName());

            Category category;
            // Zapisz nową kategorię do bazy danych
            category = existingCategory.orElseGet(() -> categoryService.createCategory(categoryToProcess));

            // Dodaj kategorię do zbioru przetworzonych kategorii
            processedCategories.add(category);

            // Dodaj książkę do kategorii, jeśli nie jest już przypisana
            category.getBooks().add(book);

            // Dodaj kategorię do książki, jeśli nie jest już przypisana
            book.getCategories().add(category);
        }

        return processedCategories;
    }


    protected Set<Author> processAuthors(Set<Author> authorsToProcess, Book book) {
        Set<Author> existingAuthors = book.getAuthors() != null ? book.getAuthors() : new HashSet<>();

        for (Author authorToProcess : authorsToProcess) {
            Optional<Author> existingAuthor = authorService.getAuthorByAuthorInfo(authorToProcess);

            Author author;
            if (existingAuthor.isPresent()) {
                author = existingAuthor.get();
            } else {
                author = authorToProcess;
                authorService.saveAuthor(author);  // Zapisz autora przed dodaniem do książki
            }

            if (!existingAuthors.contains(author)) {
                existingAuthors.add(author);
                author.getBooks().add(book);
            }
        }

        return existingAuthors;
    }
}
