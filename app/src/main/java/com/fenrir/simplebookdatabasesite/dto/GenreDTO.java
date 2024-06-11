package com.fenrir.simplebookdatabasesite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.server.core.Relation;

@Getter
@AllArgsConstructor
@Relation(itemRelation = "genre", collectionRelation = "items")
public class GenreDTO {
    private final Long id;
    private final String name;
}
