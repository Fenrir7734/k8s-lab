package com.fenrir.simplebookdatabasesite.assembler;

import com.fenrir.simplebookdatabasesite.controller.ShelfController;
import com.fenrir.simplebookdatabasesite.controller.UserController;
import com.fenrir.simplebookdatabasesite.dto.UserDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserDTO, EntityModel<UserDTO>> {
    @Override
    public EntityModel<UserDTO> toModel(UserDTO user) {
        return EntityModel.of(
                user,
                linkTo(methodOn(UserController.class).getUserInfoByUsername(user.getUsername())).withSelfRel(),
                linkTo(methodOn(ShelfController.class).getAllShelves(null, Optional.of(user.getUsername()), Optional.empty())).withRel("Shelf"),
                linkTo(methodOn(UserController.class).getAllUsers(null)).withRel("User")
        );
    }
}
