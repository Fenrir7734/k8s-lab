package com.fenrir.simplebookdatabasesite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@Relation(itemRelation = "token", collectionRelation = "tokens")
public class JwtTokenDTO {

    @NotBlank
    private String accessToken;

    @NotBlank
    private String tokenType;

    public JwtTokenDTO(String accessToken) {
        this.accessToken = accessToken;
        this.tokenType = "Bearer";
    }
}
