package com.fenrir.simplebookdatabasesite.service;

import com.fenrir.simplebookdatabasesite.dto.GenreDTO;
import com.fenrir.simplebookdatabasesite.dto.mapper.GenreMapper;
import com.fenrir.simplebookdatabasesite.exception.exceptions.ResourceCreationException;
import com.fenrir.simplebookdatabasesite.exception.exceptions.ResourceNotFoundException;
import com.fenrir.simplebookdatabasesite.model.Genre;
import com.fenrir.simplebookdatabasesite.repository.GenreRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@AllArgsConstructor
@Service
public class GenreService {
    private GenreRepository genreRepository;
    private GenreMapper genreMapper;

    public GenreDTO get(Long id) {
        Genre genre = getById(id);
        return genreMapper.toGenreDTO(genre);
    }

    public Page<GenreDTO> getAll(Pageable pageable) {
        return genreRepository.findAll(pageable)
                .map(genreMapper::toGenreDTO);
    }

    public GenreDTO create(GenreDTO genreDTO) {
        if (genreDTO.getId() != null) {
            throw new ResourceCreationException("ID value is prohibited during resource creation");
        }
        @Valid Genre genre = genreMapper.fromGenreDTO(genreDTO);
        genre = genreRepository.save(genre);
        return genreMapper.toGenreDTO(genre);
    }

    public GenreDTO update(Long id, GenreDTO genreDTO) {
        @Valid Genre updatedGenre = genreMapper.fromGenreDTO(genreDTO);
        Genre genre = getById(id);
        genre.setName(updatedGenre.getName());
        genre = genreRepository.save(genre);
        return genreMapper.toGenreDTO(genre);
    }

    public void delete(Long id) {
        Genre genre = getById(id);
        genreRepository.delete(genre);
    }

    public Genre getById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Genre was not found for id=%s", id)
                ));
    }
}
