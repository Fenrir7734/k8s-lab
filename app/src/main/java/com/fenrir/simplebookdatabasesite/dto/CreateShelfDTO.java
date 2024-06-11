package com.fenrir.simplebookdatabasesite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.server.core.Relation;

@AllArgsConstructor
@Getter
@Relation(itemRelation = "shelf", collectionRelation = "shelves")
public class CreateShelfDTO {
    private String content;
    private Integer rate;
}
