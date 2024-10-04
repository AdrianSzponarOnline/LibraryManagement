package com.librarymanagement.LibraryManagement.author;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByFirstNameAndLastName(String firstName, String lastName);
    Optional<Author> findAuthorByFirstNameAndLastNameAndNationalityAndDateOfBirth(String firstName, String lastName, String nationality, LocalDate dateOfBirth);

    boolean existsById(Long id);

    @Modifying
    @Query(value = "DELETE FROM book_author WHERE book_id = :bookId", nativeQuery = true)
    void deleteAuthorsByBookId(@Param("bookId") Long bookId);
}
