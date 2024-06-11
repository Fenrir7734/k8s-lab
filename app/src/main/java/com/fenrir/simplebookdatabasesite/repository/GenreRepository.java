package com.fenrir.simplebookdatabasesite.repository;

import com.fenrir.simplebookdatabasesite.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
