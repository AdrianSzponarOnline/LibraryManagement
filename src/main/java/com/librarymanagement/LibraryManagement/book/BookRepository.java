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
    Optional<Book> findBookByTitleAndAuthorsAndYear(String title, Set<Author> authors, int year);
    @Modifying
    @Query(value = "DELETE FROM book_author WHERE author_id = :authorId", nativeQuery = true)
    void deleteBooksByAuthorId(@Param("authorId") long authorId);
}