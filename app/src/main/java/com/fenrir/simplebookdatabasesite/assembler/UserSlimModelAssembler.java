package com.fenrir.simplebookdatabasesite.assembler;

import com.fenrir.simplebookdatabasesite.controller.ShelfController;
import com.fenrir.simplebookdatabasesite.controller.UserController;
import com.fenrir.simplebookdatabasesite.dto.UserSlimDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserSlimModelAssembler implements RepresentationModelAssembler<UserSlimDTO, EntityModel<UserSlimDTO>> {
    @Override
    public EntityModel<UserSlimDTO> toModel(UserSlimDTO user) {
        return EntityModel.of(
                user,
                linkTo(methodOn(UserController.class).getUserByUsername(user.getUsername())).withSelfRel(),
                linkTo(methodOn(ShelfController.class).getAllShelves(null, Optional.of(user.getUsername()), Optional.empty())).withRel("Shelf"),
                linkTo(methodOn(UserController.class).getAllUsers(null)).withRel("Users")
        );
    }
}
