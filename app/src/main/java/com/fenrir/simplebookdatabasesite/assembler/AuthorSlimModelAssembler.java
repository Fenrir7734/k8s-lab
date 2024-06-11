package com.fenrir.simplebookdatabasesite.assembler;

import com.fenrir.simplebookdatabasesite.controller.AuthorController;
import com.fenrir.simplebookdatabasesite.dto.AuthorSlimDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AuthorSlimModelAssembler implements RepresentationModelAssembler<AuthorSlimDTO, EntityModel<AuthorSlimDTO>> {
    @Override
    public EntityModel<AuthorSlimDTO> toModel(AuthorSlimDTO author) {
        return EntityModel.of(
                author,
                linkTo(methodOn(AuthorController.class).getAuthorById(author.getId())).withSelfRel(),
                linkTo(methodOn(AuthorController.class).getAllAuthors(null)).withRel("Authors")
        );
    }
}
