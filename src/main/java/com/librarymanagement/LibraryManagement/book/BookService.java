package com.librarymanagement.LibraryManagement.book;

import com.librarymanagement.LibraryManagement.author.Author;
import com.librarymanagement.LibraryManagement.author.AuthorRepository;
import com.librarymanagement.LibraryManagement.author.dto.AuthorMapper;
import com.librarymanagement.LibraryManagement.author.dto.BaseAuthorDTO;
import com.librarymanagement.LibraryManagement.author.exception.AuthorNotFoundException;
import com.librarymanagement.LibraryManagement.book.dto.BookDTO;
import com.librarymanagement.LibraryManagement.book.exception.BookAlreadyExistsException;
import com.librarymanagement.LibraryManagement.book.exception.BookNotFoundException;
import com.librarymanagement.LibraryManagement.category.Category;
import com.librarymanagement.LibraryManagement.category.CategoryRepository;
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
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Book saveBook(Book toSave) throws BookAlreadyExistsException {
        // Process authors and categories if present, otherwise assign empty sets
        Set<Author> authors = new HashSet<>();
        Set<Category> categories = new HashSet<>();

        if (toSave.getAuthors() != null && !toSave.getAuthors().isEmpty()) {
            authors = processAuthors(toSave.getAuthors(), toSave);
        }

        if (toSave.getCategories() != null && !toSave.getCategories().isEmpty()) {
            categories = processCategories(toSave.getCategories(), toSave);
        }

        // Set authors and categories to the book
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

    public Optional<Author> findByAuthorInfo(Author author) {
        return authorRepository.findAuthorByFirstNameAndLastNameAndNationalityAndDateOfBirth(
                author.getFirstName(),
                author.getLastName(),
                author.getNationality(),
                author.getDateOfBirth()
        );
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
    public void deleteBookById(long id) {
        Book toDelete = findBookOrThrow(id);
        bookRepository.delete(toDelete);
    }

    // helper methods
    public Book findBookOrThrow(long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with id " + id + " not found"));
    }


    @Transactional
    public Book addCategoryToBook(long bookId, CategoryDTO categoryToAdd) {
        Book book = findBookOrThrow(bookId);
        Category newCategory = CategoryMapper.toCategory(categoryToAdd);
        Optional<Category> categoryInDb = categoryRepository.findByCategoryName(newCategory.getCategoryName());
        if (categoryInDb.isPresent()) {
            newCategory = categoryInDb.get();
        } else {
            categoryRepository.save(newCategory);
        }
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
            Optional<Category> existingCategory = categoryRepository.findByCategoryName(categoryToProcess.getCategoryName());

            Category category;
            if (existingCategory.isPresent()) {
                category = existingCategory.get();
            } else {
                // Zapisz nową kategorię do bazy danych
                category = categoryRepository.save(categoryToProcess);
            }

            // Dodaj kategorię do zbioru przetworzonych kategorii
            processedCategories.add(category);

            // Dodaj książkę do kategorii, jeśli nie jest już przypisana
            if (!category.getBooks().contains(book)) {
                category.getBooks().add(book);
            }

            // Dodaj kategorię do książki, jeśli nie jest już przypisana
            if (!book.getCategories().contains(category)) {
                book.getCategories().add(category);
            }
        }

        return processedCategories;
    }


    protected Set<Author> processAuthors(Set<Author> authorsToProcess, Book book) {
        Set<Author> existingAuthors = book.getAuthors() != null ? book.getAuthors() : new HashSet<>();

        for (Author authorToProcess : authorsToProcess) {
            Optional<Author> existingAuthor = authorRepository.findAuthorByFirstNameAndLastNameAndNationalityAndDateOfBirth(
                    authorToProcess.getFirstName(),
                    authorToProcess.getLastName(),
                    authorToProcess.getNationality(),
                    authorToProcess.getDateOfBirth()
            );

            Author author;
            if (existingAuthor.isPresent()) {
                author = existingAuthor.get();
            } else {
                author = authorToProcess;
                authorRepository.save(author);  // Zapisz autora przed dodaniem do książki
            }

            if (!existingAuthors.contains(author)) {
                existingAuthors.add(author);
                author.getBooks().add(book);
            }
        }

        return existingAuthors;
    }





}
