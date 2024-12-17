package com.librarymanagement.LibraryManagement.book;

import com.librarymanagement.LibraryManagement.author.Author;
import com.librarymanagement.LibraryManagement.author.AuthorService;
import com.librarymanagement.LibraryManagement.book.exception.BookAlreadyExistsException;
import com.librarymanagement.LibraryManagement.book.exception.BookNotFoundException;
import com.librarymanagement.LibraryManagement.category.Category;
import com.librarymanagement.LibraryManagement.category.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;



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
        if (checkIfBookExists(toSave)) {
            throw new BookAlreadyExistsException("Book already exists");
        }

        // Save the book to persist the relationships
        return bookRepository.save(toSave);
    }


        //Read

    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Book getBookById(long id) {
        return findBookOrThrow(id);
    }


    @Transactional
    public Book updateBook(long id, Book toUpdate) {
        Book inDb = findBookOrThrow(id);

        // updating basic fields
        inDb.setTitle(toUpdate.getTitle());
        inDb.setIsbn(toUpdate.getIsbn());
        inDb.setYear(toUpdate.getYear());

        // updating authors
        if (toUpdate.getAuthors() != null && !toUpdate.getAuthors().isEmpty()) {
            Set<Author> updatedAuthors = processAuthors(toUpdate.getAuthors(), inDb);
            inDb.setAuthors(updatedAuthors);  // updating authors in entity from database
        }

        // Aktualizacja kategorii
        if (toUpdate.getCategories() != null && !toUpdate.getCategories().isEmpty()) {
            Set<Category> updatedCategories = processCategories(new HashSet<>(toUpdate.getCategories()), inDb);
            inDb.setCategories(updatedCategories);  // updating categories in entity from db
        }

        // saving updated book
        return bookRepository.save(inDb);
    }

    @Transactional
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
    public Book addAuthorToBook(long bookId, Author author) {
        // Find book or throw an exception if book does not exist
        Book book = findBookOrThrow(bookId);

        // Checking if book already is associated with author
        if (book.getAuthors().stream().anyMatch(existingAuthor ->
                existingAuthor.getFirstName().equals(author.getFirstName()) &&
                        existingAuthor.getLastName().equals(author.getLastName()) &&
                        existingAuthor.getNationality().equals(author.getNationality()) &&
                        existingAuthor.getDateOfBirth().equals(author.getDateOfBirth())
        )) {
            throw new IllegalArgumentException("Book with id " + bookId + " already contains this author");
        }


        // Checking if author is present in db
        Optional<Author> authorInDb = authorService.getAuthorByAuthorInfo(
                author.getFirstName(),
                author.getLastName(),
                author.getNationality(),
                author.getDateOfBirth()
        );

        if (authorInDb.isPresent()) {
            // If author does exist in db, associate him with book
            Author existingAuthor = authorInDb.get();
            book.getAuthors().add(existingAuthor);
            // Updating bidirectional relationship
            existingAuthor.getBooks().add(book);
        } else {
            // if author does not exist in db, create new entity and build relationships
            authorService.saveAuthor(author);
            book.getAuthors().add(author);
            author.getBooks().add(book);
        }

        // Save updated book and return result
        return bookRepository.save(book);
    }

    @Transactional
    public Book removeAuthorFromBook(long bookId, long authorId) {
        // Check if book exists in db or throw an exception
        Book book = findBookOrThrow(bookId);

        // same here
        Author author = authorService.findAuthorOrThrow(authorId);

        // check if author is associated with book
        if (!book.getAuthors().contains(author)) {
            throw new IllegalArgumentException("Author with id " + authorId + " is not associated with book id " + bookId);
        }

        book.removeAuthor(author);
        author.removeBook(book);

        return bookRepository.save(book);
    }

    @Transactional
    public Book addCategoryToBook(long bookId, Category categoryToAdd) {
        Book book = findBookOrThrow(bookId);
        categoryService.getByName(categoryToAdd.getCategoryName());

        book.addCategory(categoryToAdd);
        return bookRepository.save(book);
    }

    private boolean checkIfBookExists(Book book) {
        Optional<Book> existingBook = bookRepository.findByTitleAndYearAndIsbn(
                book.getTitle(),
                book.getYear(),
                book.getIsbn()
        );
        return existingBook.isPresent();
    }

    //method to process categories from set, checking if they are persisted in db or associated with book
    @Transactional
    protected Set<Category> processCategories(Set<Category> categories, Book book) {
        Set<Category> processedCategories = new HashSet<>();

        for (Category category : categories) {
            Optional<Category> categoryInDB = categoryService.getByName(category.getCategoryName());

            if (categoryInDB.isEmpty()) {
                // If the category does not exist in the database, save it
                categoryService.createCategory(category);
                processedCategories.add(category);
            } else {
                // Use the existing category from the database
                processedCategories.add(categoryInDB.get());
            }
        }

        book.setCategories(processedCategories);
        return processedCategories;
    }


    @Transactional
    protected Set<Author> processAuthors(Set<Author> authorsToProcess, Book book) {
        Set<Author> existingAuthors = book.getAuthors() != null ? book.getAuthors() : new HashSet<>();

        for (Author authorToProcess : authorsToProcess) {
            Optional<Author> existingAuthor = authorService.getAuthorByAuthorInfo(
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
