package com.fenrir.simplebookdatabasesite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.server.core.Relation;

@AllArgsConstructor
@Getter
@Relation(itemRelation = "password", collectionRelation = "passwords")
public class NewPasswordDTO {
    private String oldPassword;
    private String newPassword;
    private String newPasswordConfirmation;
}
