package com.fenrir.simplebookdatabasesite.dto.mapper;

import com.fenrir.simplebookdatabasesite.dto.AuthorDTO;
import com.fenrir.simplebookdatabasesite.dto.AuthorSlimDTO;
import com.fenrir.simplebookdatabasesite.model.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {
    public AuthorDTO toAuthorDTO(Author author) {
        return new AuthorDTO(
                author.getId(),
                author.getFirstname(),
                author.getLastname(),
                author.getDescription(),
                author.getBirthDate(),
                author.getDeathDate()
        );
    }

    public AuthorSlimDTO toAuthorSlimDTO(Author author) {
        return new AuthorSlimDTO(
                author.getId(),
                author.getFullName()
        );
    }

    public Author fromAuthorDTO(AuthorDTO authorDTO) {
        return new Author(
                authorDTO.getId(),
                authorDTO.getFirstname(),
                authorDTO.getLastname(),
                authorDTO.getDescription(),
                authorDTO.getBirthDate(),
                authorDTO.getDeathDate()
        );
    }
}
