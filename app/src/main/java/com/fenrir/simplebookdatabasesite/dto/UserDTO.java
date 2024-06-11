package com.fenrir.simplebookdatabasesite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Relation(itemRelation = "user", collectionRelation = "users")
public class UserDTO {
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private Boolean verified;
    private Boolean banned;
    private LocalDateTime createdAt;
}
