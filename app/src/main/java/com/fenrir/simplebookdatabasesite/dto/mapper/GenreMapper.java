package com.fenrir.simplebookdatabasesite.dto.mapper;

import com.fenrir.simplebookdatabasesite.dto.GenreDTO;
import com.fenrir.simplebookdatabasesite.model.Genre;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper {
    public GenreDTO toGenreDTO(Genre genre) {
        return new GenreDTO(
                genre.getId(),
                genre.getName()
        );
    }

    public Genre fromGenreDTO(GenreDTO genre) {
        return new Genre(
                genre.getId(),
                genre.getName()
        );
    }
}
