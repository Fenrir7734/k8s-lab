package com.fenrir.simplebookdatabasesite.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.server.core.Relation;

@AllArgsConstructor
@Getter
@Relation(itemRelation = "book", collectionRelation = "books")
public class BookSlimDTO {
    private Long id;
    private String title;
    private String description;
    private byte[] cover;

    @JsonIgnore
    private Long authorId;

    @JsonProperty(value = "author")
    private String authorName;
}
