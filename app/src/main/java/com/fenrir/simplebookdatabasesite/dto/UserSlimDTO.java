package com.fenrir.simplebookdatabasesite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Relation(itemRelation = "user", collectionRelation = "users")
public class UserSlimDTO {
    private String username;
    private LocalDateTime createdAt;
    private Boolean banned;
}
