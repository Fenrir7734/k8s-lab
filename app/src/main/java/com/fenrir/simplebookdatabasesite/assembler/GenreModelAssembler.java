package com.fenrir.simplebookdatabasesite.assembler;

import com.fenrir.simplebookdatabasesite.controller.GenreController;
import com.fenrir.simplebookdatabasesite.dto.GenreDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GenreModelAssembler implements RepresentationModelAssembler<GenreDTO, EntityModel<GenreDTO>> {
    @Override
    public EntityModel<GenreDTO> toModel(GenreDTO genre) {
        return EntityModel.of(
                genre,
                linkTo(methodOn(GenreController.class).getGenreById(genre.getId())).withSelfRel(),
                linkTo(methodOn(GenreController.class).getAllGenres(null)).withRel("Genres")
        );
    }
}
