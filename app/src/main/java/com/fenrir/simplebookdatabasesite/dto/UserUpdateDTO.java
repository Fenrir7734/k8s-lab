package com.fenrir.simplebookdatabasesite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.server.core.Relation;

@AllArgsConstructor
@Getter
@Relation(itemRelation = "user", collectionRelation = "user")
public class UserUpdateDTO {
    private String firstname;
    private String lastname;
    private String phone;
}
