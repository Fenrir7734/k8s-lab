package com.fenrir.simplebookdatabasesite.assembler;

import com.fenrir.simplebookdatabasesite.dto.ShelfDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ShelfModelAssembler implements RepresentationModelAssembler<ShelfDTO, EntityModel<ShelfDTO>> {
    @Override
    public EntityModel<ShelfDTO> toModel(ShelfDTO shelf) {
        return EntityModel.of(
                shelf
        );
    }
}
