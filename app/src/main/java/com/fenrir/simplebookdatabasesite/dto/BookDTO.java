package com.fenrir.simplebookdatabasesite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Relation(itemRelation = "book", collectionRelation = "books")
public class BookDTO {
    private Long id;
    private String isbn;
    private String title;
    private LocalDate published;
    private Integer pages;
    private String description;
    private Long authorId;
    private Long genreId;
}
