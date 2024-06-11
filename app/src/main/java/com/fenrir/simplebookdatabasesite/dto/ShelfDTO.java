package com.fenrir.simplebookdatabasesite.dto;

import com.fenrir.simplebookdatabasesite.model.Shelf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Relation(itemRelation = "shelf", collectionRelation = "shelves")
public class ShelfDTO {
    private Shelf.Id id;
    private String content;
    private Integer rate;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
