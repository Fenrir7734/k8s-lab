package com.fenrir.simplebookdatabasesite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.server.core.Relation;

@Getter
@AllArgsConstructor
@Relation(itemRelation = "author", collectionRelation = "authors")
public class AuthorSlimDTO {
    private final Long id;
    private final String name;
}
