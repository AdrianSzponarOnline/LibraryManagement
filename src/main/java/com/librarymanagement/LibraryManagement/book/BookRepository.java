package com.librarymanagement.LibraryManagement.book;

import com.librarymanagement.LibraryManagement.author.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitleAndYearAndIsbn(String title, int year, String isbn);
    @Modifying
    @Query(value = "DELETE FROM book_author WHERE author_id = :authorId", nativeQuery = true)
    void deleteBooksByAuthorId(@Param("authorId") long authorId);
    @Query("SELECT b FROM Book b " +
            "LEFT JOIN FETCH b.authors " +
            "LEFT JOIN FETCH b.categories " +
            "WHERE b.id = :id")
    Book findBookWithAuthorsAndCategories(@Param("id") Long id);


}