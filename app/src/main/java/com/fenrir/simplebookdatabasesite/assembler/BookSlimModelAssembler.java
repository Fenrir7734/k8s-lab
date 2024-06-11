package com.fenrir.simplebookdatabasesite.assembler;

import com.fenrir.simplebookdatabasesite.controller.AuthorController;
import com.fenrir.simplebookdatabasesite.controller.BookController;
import com.fenrir.simplebookdatabasesite.dto.BookSlimDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BookSlimModelAssembler implements RepresentationModelAssembler<BookSlimDTO, EntityModel<BookSlimDTO>> {
    @Override
    public EntityModel<BookSlimDTO> toModel(BookSlimDTO book) {
        return EntityModel.of(
                book,
                linkTo(methodOn(AuthorController.class).getAuthorById(book.getAuthorId())).withRel("Author"),
                linkTo(methodOn(BookController.class).getBookById(book.getId())).withSelfRel(),
                linkTo(methodOn(BookController.class).getAllBooks(null, Optional.empty(), Optional.empty())).withRel("Books")
        );
    }
}
