package com.fenrir.simplebookdatabasesite.repository;

import com.fenrir.simplebookdatabasesite.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findAllByGenre_Name(String genreName, Pageable pageable);
    Page<Book> findAllByAuthor_Id(Long authorId, Pageable pageable);
}
