package com.fenrir.simplebookdatabasesite.controller;

import com.fenrir.simplebookdatabasesite.assembler.BookStatisticsModelAssembler;
import com.fenrir.simplebookdatabasesite.assembler.ShelfModelAssembler;
import com.fenrir.simplebookdatabasesite.dto.StatisticsDTO;
import com.fenrir.simplebookdatabasesite.dto.CreateShelfDTO;
import com.fenrir.simplebookdatabasesite.dto.ShelfDTO;
import com.fenrir.simplebookdatabasesite.exception.message.ErrorMessage;
import com.fenrir.simplebookdatabasesite.service.ShelfService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.fenrir.simplebookdatabasesite.common.Constants.ALLOWED_ORIGIN;
import static com.fenrir.simplebookdatabasesite.common.Constants.APPLICATION_HAL_JSON;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = ALLOWED_ORIGIN)
@RequestMapping(
        path = "/api/shelves",
        produces = APPLICATION_HAL_JSON
)
public class ShelfController {
    private final ShelfService shelfService;
    private final ShelfModelAssembler shelfAssembler;
    private final BookStatisticsModelAssembler bookStatisticsAssembler;
    private final PagedResourcesAssembler<ShelfDTO> pagedAssembler;

    @GetMapping(path = "/{username}/{bookId}")
    public ResponseEntity<?> getShelfByUsernameAndBookId(
            @PathVariable("username") String username,
            @PathVariable("bookId") Long bookId) {

        ShelfDTO shelf = shelfService.get(username, bookId);
        return ResponseEntity.ok(shelfAssembler.toModel(shelf));
    }

    @GetMapping
    public ResponseEntity<?> getAllShelves(
            @PageableDefault Pageable pageable,
            @RequestParam("username") Optional<String> username,
            @RequestParam("bookId") Optional<Long> bookId) {

        if (username.isPresent() && bookId.isPresent()) {
            ErrorMessage error = new ErrorMessage(
                    HttpStatus.BAD_REQUEST.value(),
                    LocalDateTime.now(),
                    "Wrong combination of parameters",
                    "username and bookId parameters are mutually exclusive"
            );
            return ResponseEntity.badRequest().body(error);
        }
        Page<ShelfDTO> shelves = shelfService.getAll(pageable, username, bookId);
        return ResponseEntity.ok(pagedAssembler.toModel(shelves, shelfAssembler));
    }

    @GetMapping("/book/{bookId}/stats")
    public ResponseEntity<?> getBookStatistics(@PathVariable("bookId") Long bookId) {
        StatisticsDTO stats = shelfService.getBooksStatistics(bookId);
        return ResponseEntity.ok(bookStatisticsAssembler.toModel(stats));
    }

    @GetMapping("/author/{authorId}/stats")
    public ResponseEntity<?> getAuthorStatistics(@PathVariable("authorId") Long authorId) {
        StatisticsDTO stats = shelfService.getAuthorStatistics(authorId);
        return ResponseEntity.ok(bookStatisticsAssembler.toModel(stats));
    }

    @PostMapping(path = "/{username}/{bookId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postShelf(
            @PathVariable("username") String username,
            @PathVariable("bookId") Long bookId,
            @RequestBody CreateShelfDTO shelfToCreate) {

        ShelfDTO shelf = shelfService.create(shelfToCreate, username, bookId);
        return new ResponseEntity<>(shelf, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{username}/{bookId}")
    public ResponseEntity<?> updateShelf(
            @PathVariable("username") String username,
            @PathVariable("bookId") Long bookId,
            @RequestBody CreateShelfDTO shelfToUpdate) {

        ShelfDTO shelf = shelfService.update(shelfToUpdate, username, bookId);
        return ResponseEntity.ok(shelfAssembler.toModel(shelf));
    }

    @DeleteMapping(path = "/{username}/{bookId}")
    public ResponseEntity<?> deleteShelfByUsernameAndBookId(
            @PathVariable("username") String username,
            @PathVariable("bookId") Long bookId) {

        shelfService.delete(username, bookId);
        return ResponseEntity.noContent().build();
    }
}
