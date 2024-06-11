package com.fenrir.simplebookdatabasesite.controller;

import com.fenrir.simplebookdatabasesite.assembler.AuthorModelAssembler;
import com.fenrir.simplebookdatabasesite.assembler.AuthorSlimModelAssembler;
import com.fenrir.simplebookdatabasesite.dto.AuthorDTO;
import com.fenrir.simplebookdatabasesite.dto.AuthorSlimDTO;
import com.fenrir.simplebookdatabasesite.service.AuthorService;
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

import javax.validation.Valid;
import java.net.URI;

import static com.fenrir.simplebookdatabasesite.common.Constants.ALLOWED_ORIGIN;
import static com.fenrir.simplebookdatabasesite.common.Constants.APPLICATION_HAL_JSON;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = ALLOWED_ORIGIN)
@RequestMapping(
        path = "/api/authors",
        produces = APPLICATION_HAL_JSON
)
public class AuthorController {
    private final AuthorService authorService;
    private final AuthorModelAssembler authorAssembler;
    private final AuthorSlimModelAssembler authorSlimAssembler;
    private final PagedResourcesAssembler<AuthorSlimDTO> pagedAssembler;

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getAuthorById(@PathVariable("id") Long id) {
        AuthorDTO author = authorService.get(id);
        return ResponseEntity.ok(authorAssembler.toModel(author));
    }

    @GetMapping
    public ResponseEntity<?> getAllAuthors(
            @PageableDefault(sort = "lastname", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<AuthorSlimDTO> authors = authorService.getAll(pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(authors, authorSlimAssembler));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postAuthor(@RequestBody @Valid AuthorDTO author, UriComponentsBuilder builder) {
        author = authorService.create(author);
        Long authorId = author.getId();
        URI location = builder.replacePath("/api/authors/{id}")
                .buildAndExpand(authorId)
                .toUri();
        return ResponseEntity.created(location).body(authorAssembler.toModel(author));
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAuthor(@PathVariable("id") Long id, @Valid @RequestBody AuthorDTO author) {
        author = authorService.update(id, author);
        return ResponseEntity.ok(authorAssembler.toModel(author));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAuthorById(@PathVariable("id") Long id) {
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
