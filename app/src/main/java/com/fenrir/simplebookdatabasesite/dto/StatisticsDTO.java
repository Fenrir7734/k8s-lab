package com.fenrir.simplebookdatabasesite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.server.core.Relation;

@Getter
@AllArgsConstructor
@Relation(itemRelation = "statistics", collectionRelation = "statistics")
public class StatisticsDTO {
    private Long id;
    private Long numberOfRates;
    private Long numberOfComments;
    private Double rate;
}
