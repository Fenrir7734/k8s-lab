package com.fenrir.simplebookdatabasesite.repository;

import com.fenrir.simplebookdatabasesite.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
