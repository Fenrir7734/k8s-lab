package com.fenrir.simplebookdatabasesite.repository;

import com.fenrir.simplebookdatabasesite.model.Shelf;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShelfRepository extends JpaRepository<Shelf, Shelf.Id> {
    Page<Shelf> findAllByBook_Id(Long id, Pageable pageable);
    List<Shelf> findAllByBook_Id(Long id);

    List<Shelf> findAllByBook_Author_Id(Long id);

    List<Shelf> findAllByUser_Credentials_Username(String username);
    Page<Shelf> findAllByUser_Credentials_Username(String username, Pageable pageable);
    Optional<Shelf> findByUser_Credentials_UsernameAndBook_Id(String username, Long bookId);
}
