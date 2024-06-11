package com.fenrir.simplebookdatabasesite.assembler;

import com.fenrir.simplebookdatabasesite.controller.BookController;
import com.fenrir.simplebookdatabasesite.controller.ShelfController;
import com.fenrir.simplebookdatabasesite.dto.StatisticsDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BookStatisticsModelAssembler implements RepresentationModelAssembler<StatisticsDTO, EntityModel<StatisticsDTO>> {
    @Override
    public EntityModel<StatisticsDTO> toModel(StatisticsDTO stats) {
        return EntityModel.of(
                stats,
                linkTo(methodOn(ShelfController.class).getBookStatistics(stats.getId())).withSelfRel()
        );
    }
}
