package com.fenrir.simplebookdatabasesite.dto.mapper;

import com.fenrir.simplebookdatabasesite.dto.StatisticsDTO;
import com.fenrir.simplebookdatabasesite.dto.CreateShelfDTO;
import com.fenrir.simplebookdatabasesite.dto.ShelfDTO;
import com.fenrir.simplebookdatabasesite.model.Book;
import com.fenrir.simplebookdatabasesite.model.Shelf;
import com.fenrir.simplebookdatabasesite.model.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class ShelfMapper {
    public ShelfDTO toShelfDTO(Shelf shelf) {
        return new ShelfDTO(
                shelf.getId(),
                shelf.getContent(),
                shelf.getRate(),
                shelf.getUpdatedAt(),
                shelf.getCreatedAt()
        );
    }

    public StatisticsDTO toStatisticsDTO(Long bookId, List<Shelf> shelves) {
        long numberOfRates = shelves.size();
        long numberOfComments = shelves.stream()
                .filter(shelf -> shelf.getContent() != null)
                .count();
        double rate = shelves.stream()
                .mapToInt(Shelf::getRate)
                .average()
                .orElse(0);
        rate = BigDecimal.valueOf(rate)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        return new StatisticsDTO(
                bookId,
                numberOfRates,
                numberOfComments,
                rate
        );
    }

    public Shelf fromCreateShelfDTO(CreateShelfDTO shelfDTO, User user, Book book) {
        return new Shelf(
                new Shelf.Id(user.getId(), book.getId()),
                shelfDTO.getContent(),
                shelfDTO.getRate()
        );
    }
}
