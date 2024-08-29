package com.librarymanagement.LibraryManagement.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitleAndGenreIdAndYear(String title, Long genreId, Integer year);
}
