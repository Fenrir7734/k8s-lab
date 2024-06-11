package com.fenrir.simplebookdatabasesite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Relation(itemRelation = "author", collectionRelation = "authors")
public class AuthorDTO {
    private final Long id;

    @NotBlank
    @Size(min = 2, max = 50)
    private final String firstname;

    @NotBlank
    @Size(min = 2, max = 50)
    private final String lastname;
    private final String description;
    private final LocalDate birthDate;
    private final LocalDate deathDate;
}
