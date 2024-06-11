package com.fenrir.simplebookdatabasesite.controller;

import com.fenrir.simplebookdatabasesite.assembler.GenreModelAssembler;
import com.fenrir.simplebookdatabasesite.dto.GenreDTO;
import com.fenrir.simplebookdatabasesite.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.fenrir.simplebookdatabasesite.common.Constants.ALLOWED_ORIGIN;
import static com.fenrir.simplebookdatabasesite.common.Constants.APPLICATION_HAL_JSON;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = ALLOWED_ORIGIN)
@RequestMapping(
        path = "/api/genres",
        produces = APPLICATION_HAL_JSON
)
public class GenreController {
    private final GenreService genreService;
    private final GenreModelAssembler genreAssembler;
    private final PagedResourcesAssembler<GenreDTO> pagedAssembler;

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getGenreById(@PathVariable("id") Long id) {
        GenreDTO genre = genreService.get(id);
        return ResponseEntity.ok(genreAssembler.toModel(genre));
    }

    @GetMapping
    public ResponseEntity<?> getAllGenres(
            @PageableDefault(sort = "name", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<GenreDTO> genres = genreService.getAll(pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(genres, genreAssembler));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postGenre(@RequestBody GenreDTO genre, UriComponentsBuilder builder) {
        genre = genreService.create(genre);
        Long genreId = genre.getId();
        URI location = builder.replacePath("/api/genres/{id}")
                .buildAndExpand(genreId)
                .toUri();
        return ResponseEntity.created(location).body(genreAssembler.toModel(genre));
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateGenre(@PathVariable("id") Long id, @RequestBody GenreDTO genre) {
        genre = genreService.update(id, genre);
        return ResponseEntity.ok(genreAssembler.toModel(genre));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGenreById(@PathVariable("id") Long id) {
        genreService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
