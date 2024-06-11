package com.fenrir.simplebookdatabasesite.service;

import com.fenrir.simplebookdatabasesite.dto.StatisticsDTO;
import com.fenrir.simplebookdatabasesite.dto.CreateShelfDTO;
import com.fenrir.simplebookdatabasesite.dto.ShelfDTO;
import com.fenrir.simplebookdatabasesite.dto.mapper.ShelfMapper;
import com.fenrir.simplebookdatabasesite.exception.exceptions.ResourceNotFoundException;
import com.fenrir.simplebookdatabasesite.model.Book;
import com.fenrir.simplebookdatabasesite.model.Shelf;
import com.fenrir.simplebookdatabasesite.model.User;
import com.fenrir.simplebookdatabasesite.repository.BookRepository;
import com.fenrir.simplebookdatabasesite.repository.ShelfRepository;
import com.fenrir.simplebookdatabasesite.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ShelfService {
    private ShelfRepository shelfRepository;
    private UserRepository userRepository;
    private BookRepository bookRepository;
    private ShelfMapper shelfMapper;

    public ShelfDTO get(String username, Long bookId) {
        Shelf shelf = getByUsernameAndBookId(username, bookId);
        return shelfMapper.toShelfDTO(shelf);
    }

    public Page<ShelfDTO> getAll(Pageable pageable, Optional<String> username, Optional<Long> bookId) {
        return username.map(s -> shelfRepository.findAllByUser_Credentials_Username(s, pageable)
                .map(shelfMapper::toShelfDTO)).orElseGet(() -> bookId.map(aLong -> shelfRepository.findAllByBook_Id(aLong, pageable)
                .map(shelfMapper::toShelfDTO)).orElseGet(() -> shelfRepository.findAll(pageable)
                .map(shelfMapper::toShelfDTO)));
    }

    public StatisticsDTO getBooksStatistics(Long bookId) {
        List<Shelf> shelves = shelfRepository.findAllByBook_Id(bookId);
        return shelfMapper.toStatisticsDTO(bookId, shelves);
    }

    public StatisticsDTO getAuthorStatistics(Long authorId) {
        List<Shelf> shelves = shelfRepository.findAllByBook_Author_Id(authorId);
        return shelfMapper.toStatisticsDTO(authorId, shelves);
    }

    public ShelfDTO create(CreateShelfDTO shelfDTO, String username, Long bookId) {
        User user = userRepository.getUserByCredentials_Username(username);
        Book book = bookRepository.getById(bookId);
        @Valid Shelf shelf = shelfMapper.fromCreateShelfDTO(shelfDTO, user, book);
        shelf = shelfRepository.save(shelf);
        return shelfMapper.toShelfDTO(shelf);
    }

    public ShelfDTO update(CreateShelfDTO shelfDTO, String username, Long bookId) {
        User user = userRepository.getUserByCredentials_Username(username);
        Book book = bookRepository.getById(bookId);
        @Valid Shelf updatedShelf = shelfMapper.fromCreateShelfDTO(shelfDTO, user, book);
        Shelf shelf = getByUsernameAndBookId(username, book.getId());
        shelf.setContent(updatedShelf.getContent());
        shelf.setRate(updatedShelf.getRate());
        shelf = shelfRepository.save(shelf);
        return shelfMapper.toShelfDTO(shelf);
    }

    public void delete(String username, Long bookId) {
        Shelf shelf = getByUsernameAndBookId(username, bookId);
        shelfRepository.delete(shelf);
    }

    public Shelf getByUsernameAndBookId(String username, Long bookId) {
        return shelfRepository.findByUser_Credentials_UsernameAndBook_Id(username, bookId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Shelf was not found for user=%s and bookId=%s", username, bookId)
                ));
    }
}
