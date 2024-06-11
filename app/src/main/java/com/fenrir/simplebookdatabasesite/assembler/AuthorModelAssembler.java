package com.fenrir.simplebookdatabasesite.assembler;

import com.fenrir.simplebookdatabasesite.controller.AuthorController;
import com.fenrir.simplebookdatabasesite.dto.AuthorDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AuthorModelAssembler implements RepresentationModelAssembler<AuthorDTO, EntityModel<AuthorDTO>> {
    @Override
    public EntityModel<AuthorDTO> toModel(AuthorDTO author) {
        return EntityModel.of(
                author,
                linkTo(methodOn(AuthorController.class).getAuthorById(author.getId())).withSelfRel(),
                linkTo(methodOn(AuthorController.class).getAllAuthors(null)).withRel("Authors")
        );
    }
}
