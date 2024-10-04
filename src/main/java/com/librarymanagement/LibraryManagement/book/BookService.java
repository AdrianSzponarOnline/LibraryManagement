package com.librarymanagement.LibraryManagement.book;

import com.librarymanagement.LibraryManagement.author.Author;
import com.librarymanagement.LibraryManagement.author.AuthorRepository;
import com.librarymanagement.LibraryManagement.author.dto.AuthorMapper;
import com.librarymanagement.LibraryManagement.author.dto.BaseAuthorDTO;
import com.librarymanagement.LibraryManagement.author.exception.AuthorNotFoundException;
import com.librarymanagement.LibraryManagement.book.dto.BookDTO;
import com.librarymanagement.LibraryManagement.book.dto.BookMapper;
import com.librarymanagement.LibraryManagement.book.dto.FullBookDTO;
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
    public Book saveBook(FullBookDTO bookDTO) {
        // Convert the DTO to an entity
        Book book = BookMapper.toEntity(bookDTO);
        System.out.println(book);

        // Check if the book already exists
        checkIfBookExists(book);
        // Process authors and categories if present, otherwise assign empty sets
        Set<Author> authors = new HashSet<>();
        Set<Category> categories = new HashSet<>();

        if (bookDTO.getAuthors() != null && !bookDTO.getAuthors().isEmpty()) {
            authors = processAuthors(new HashSet<>(bookDTO.getAuthors()), book);
        }

        if (bookDTO.getCategories() != null && !bookDTO.getCategories().isEmpty()) {
            categories = processCategories(new HashSet<>(bookDTO.getCategories()), book);
        }

        // Set authors and categories to the book
        book.setAuthors(authors);
        book.setCategories(categories);

        // Save the book again to persist the relationships
        return bookRepository.save(book);
    }


    //Read

    public List<BookDTO> getAllBooks() {
        List<BookDTO> bookDTOs = new ArrayList<>();
        for (Book book : bookRepository.findAll()) {
            bookDTOs.add(toDto(book));
        }
        return bookDTOs;
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
    public Book updateBook(long id, FullBookDTO bookDTO) {
        Book toUpdate = findBookOrThrow(id);

        // Update podstawowych pól
        toUpdate.setTitle(bookDTO.getTitle());
        toUpdate.setIsbn(bookDTO.getIsbn());
        toUpdate.setYear(bookDTO.getYear());

        // Aktualizacja autorów
        if (bookDTO.getAuthors() != null && !bookDTO.getAuthors().isEmpty()) {
            Set<Author> updatedAuthors = processAuthors(new HashSet<>(bookDTO.getAuthors()), toUpdate);
            toUpdate.setAuthors(updatedAuthors);
        }

        // Aktualizacja kategorii
        if (bookDTO.getCategories() != null && !bookDTO.getCategories().isEmpty()) {
            Set<Category> updatedCategories = processCategories(new HashSet<>(bookDTO.getCategories()), toUpdate);
            toUpdate.setCategories(updatedCategories);
        }

        // Zapisz zaktualizowaną książkę
        return bookRepository.save(toUpdate);
    }



    //Delete
    public void deleteBookById(long id) {
        Book toDelete = findBookOrThrow(id);
        bookRepository.delete(toDelete);
    }

    // helper methods
    private Book findBookOrThrow(long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with id " + id + " not found"));
    }


    @Transactional
    public Book addAuthorsToBook(long bookId, BaseAuthorDTO authorToAdd) {
        Book book = findBookOrThrow(bookId);
        Author newAuthor = AuthorMapper.toEntity(authorToAdd);
        Optional<Author> authorInDb = findByAuthorInfo(newAuthor);

        if (authorInDb.isPresent()) {
            newAuthor = authorInDb.get();
        } else {
            authorRepository.save(newAuthor);
        }

        // Helper method adds entries on both side of relationship
        book.addAuthor(newAuthor);

        // Adding entries in the intermediate method
        return bookRepository.save(book);
    }

    @Transactional
    public Book removeAuthorFormBook(long bookId, BaseAuthorDTO authorToRemove) {
        Book book = findBookOrThrow(bookId);
        Optional<Author> authorInDb = findByAuthorInfo(AuthorMapper.toEntity(authorToRemove));

        if (authorInDb.isPresent()) {
            Author author = authorInDb.get();
            book.removeAuthor(author);
            return bookRepository.save(book);
        } else {
            throw new AuthorNotFoundException("Author not found");
        }
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
    protected Set<Author> processAuthors(Set<BaseAuthorDTO> authorDTOs, Book book) {
        Set<Author> authors = book.getAuthors() != null ? book.getAuthors() : new HashSet<>();

        for (BaseAuthorDTO authorDTO : authorDTOs) {
            Optional<Author> existingAuthor = authorRepository.findAuthorByFirstNameAndLastNameAndNationalityAndDateOfBirth(
                    authorDTO.getFirstName(),
                    authorDTO.getLastName(),
                    authorDTO.getNationality(),
                    authorDTO.getDateOfBirth()
            );

            Author author;
            if (existingAuthor.isPresent()) {
                author = existingAuthor.get();
            } else {
                author = AuthorMapper.toEntity(authorDTO);
                authorRepository.save(author);
            }

            // Dodaj autora, jeśli go jeszcze nie ma w zbiorze
            if (!authors.contains(author)) {
                authors.add(author);
                author.getBooks().add(book);  // Zarządzaj relacją po obu stronach
            }
        }

        return authors;
    }


    @Transactional
    protected Set<Category> processCategories(Set<CategoryDTO> categoryDTOs, Book book) {
        Set<Category> categories = book.getCategories() != null ? book.getCategories() : new HashSet<>();

        for (CategoryDTO categoryDTO : categoryDTOs) {
            Optional<Category> existingCategory = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());

            Category category;
            if (existingCategory.isPresent()) {
                category = existingCategory.get();
            } else {
                category = CategoryMapper.toCategory(categoryDTO);
                categoryRepository.save(category);
            }

            // Dodaj kategorię, jeśli jej jeszcze nie ma w zbiorze
            if (!categories.contains(category)) {
                categories.add(category);
                category.getBooks().add(book);  // Zarządzaj relacją po obu stronach
            }
        }

        return categories;
    }



}
