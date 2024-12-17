package com.librarymanagement.LibraryManagement.author;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Page<Author> findAll(Pageable pageable);
    Optional<Author> findByFirstNameAndLastName(String firstName, String lastName);

    Optional<Author> findAuthorByFirstNameAndLastNameAndNationalityAndDateOfBirth(String firstName, String lastName, String nationality, LocalDate dateOfBirth);

    boolean existsById(Long id);

    @Modifying
    @Query(value = "DELETE FROM book_author WHERE book_id = :bookId", nativeQuery = true)
    void deleteAuthorsByBookId(@Param("bookId") Long bookId);



    @Modifying
    @Query(value = "DELETE FROM book_author ba where ba.author_id = :authorId", nativeQuery = true)
    void deleteAuthorByIdFromBooks(@Param("authorId") long authorId);

    @Query(value = "SELECT COUNT(*) > 0 FROM author a " +
            "WHERE a.first_name = :firstName AND " +
            "a.last_name = :lastName AND " +
            "a.nationality = :nationality AND " +
            "a.date_of_birth = :dateOfBirth", nativeQuery = true)
    boolean existsByAuthorInfo(@Param("firstName") String firstName,
                               @Param("lastName") String lastName,
                               @Param("nationality") String nationality,
                               @Param("dateOfBirth") LocalDate dateOfBirth);


    @Query("SELECT a FROM Author a WHERE a.firstName IN :firstNames AND a.lastName IN :lastNames AND a.nationality IN :nationalities AND a.dateOfBirth IN :datesOfBirth")
    List<Author> findAllByAuthorInfo(@Param("firstNames") Set<String> firstNames,
                                     @Param("lastNames") Set<String> lastNames,
                                     @Param("nationalities") Set<String> nationalities,
                                     @Param("datesOfBirth") Set<LocalDate> datesOfBirth);

    @Query("SELECT a FROM Author a WHERE a.firstName IN :firstNames AND a.lastName IN :lastNames AND a.nationality IN :nationality AND a.dateOfBirth IN :datesOfBirth")
    Optional<Author> findByAuthorInfo(@Param("firstNames") String firstName,
                                     @Param("lastNames") String lastName,
                                     @Param("nationality") String nationality,
                                     @Param("datesOfBirth") LocalDate datesOfBirth);

}

