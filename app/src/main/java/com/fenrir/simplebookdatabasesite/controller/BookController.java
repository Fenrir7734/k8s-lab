package com.fenrir.simplebookdatabasesite.controller;

import com.fenrir.simplebookdatabasesite.assembler.BookModelAssembler;
import com.fenrir.simplebookdatabasesite.assembler.BookSlimModelAssembler;
import com.fenrir.simplebookdatabasesite.dto.BookDTO;
import com.fenrir.simplebookdatabasesite.dto.BookSlimDTO;
import com.fenrir.simplebookdatabasesite.service.BookService;
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
import java.util.Optional;

import static com.fenrir.simplebookdatabasesite.common.Constants.ALLOWED_ORIGIN;
import static com.fenrir.simplebookdatabasesite.common.Constants.APPLICATION_HAL_JSON;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = ALLOWED_ORIGIN)
@RequestMapping(
        path = "/api/books",
        produces = APPLICATION_HAL_JSON
)
public class BookController {
    private final BookService bookService;
    private final BookModelAssembler bookAssembler;
    private final BookSlimModelAssembler bookSlimAssembler;
    private final PagedResourcesAssembler<BookSlimDTO> pagedAssembler;

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getBookById(@PathVariable("id") Long id) {
        BookDTO book = bookService.get(id);
        return ResponseEntity.ok(bookAssembler.toModel(book));
    }

    @GetMapping(path = "/{id}/cover", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getBookCoverById(@PathVariable("id") Long id) {
        return bookService.getCover(id);
    }

    @GetMapping
    public ResponseEntity<?> getAllBooks(
            @PageableDefault(sort = "title", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam("genre") Optional<String> genre,
            @RequestParam("author") Optional<Long> authorId) {

        Page<BookSlimDTO> books = bookService.getAll(pageable, genre, authorId);
        return ResponseEntity.ok(pagedAssembler.toModel(books, bookSlimAssembler));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postBook(@RequestBody BookDTO book, UriComponentsBuilder builder) {
        book = bookService.create(book);
        Long bookId = book.getId();
        URI location = builder.replacePath("/api/books/{id}")
                .buildAndExpand(bookId)
                .toUri();
        return ResponseEntity.created(location).body(bookAssembler.toModel(book));
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateBook(@PathVariable("id") Long id, @RequestBody BookDTO book) {
        book = bookService.update(id, book);
        return ResponseEntity.ok(bookAssembler.toModel(book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAuthorById(@PathVariable("id") Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
