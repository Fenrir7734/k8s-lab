package com.fenrir.simplebookdatabasesite.service;

import com.fenrir.simplebookdatabasesite.dto.BookDTO;
import com.fenrir.simplebookdatabasesite.dto.BookSlimDTO;
import com.fenrir.simplebookdatabasesite.dto.mapper.BookMapper;
import com.fenrir.simplebookdatabasesite.exception.exceptions.ResourceCreationException;
import com.fenrir.simplebookdatabasesite.exception.exceptions.ResourceNotFoundException;
import com.fenrir.simplebookdatabasesite.model.Book;
import com.fenrir.simplebookdatabasesite.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.*;

@AllArgsConstructor
@Service
public class BookService {
    private BookRepository bookRepository;
    private BookMapper bookMapper;

    public BookDTO get(Long id) {
        Book book = getById(id);
        return bookMapper.toBookDTO(book);
    }

    public byte[] getCover(Long id) {
        Book book = getById(id);
        return book.getCover();
    }

    public Page<BookSlimDTO> getAll(Pageable pageable, Optional<String> genre, Optional<Long> authorId) {
        if (genre.isPresent()) {
            return bookRepository.findAllByGenre_Name(genre.get(), pageable).map(bookMapper::toBookSlimDTO);
        }
        if (authorId.isPresent()) {
            return bookRepository.findAllByAuthor_Id(authorId.get(), pageable).map(bookMapper::toBookSlimDTO);
        }
        return bookRepository.findAll(pageable).map(bookMapper::toBookSlimDTO);
    }

    public BookDTO create(BookDTO bookSaveDTO) {
        if (bookSaveDTO.getId() != null) {
            throw new ResourceCreationException("ID value is prohibited during resource creation");
        }
        Book book = bookMapper.fromBookDTO(bookSaveDTO);
        book = bookRepository.save(book);
        return bookMapper.toBookDTO(book);
    }

    public BookDTO update(Long id, BookDTO bookDTO) {
        @Valid Book updatedBook = bookMapper.fromBookDTO(bookDTO);
        Book book = getById(id);
        book.setIsbn(updatedBook.getIsbn());
        book.setTitle(updatedBook.getTitle());
        book.setPublished(updatedBook.getPublished());
        book.setPages(updatedBook.getPages());
        book.setDescription(updatedBook.getDescription());
        book.setCover(updatedBook.getCover());
        book.setAuthor(updatedBook.getAuthor());
        book.setGenre(updatedBook.getGenre());
        book = bookRepository.save(book);
        return bookMapper.toBookDTO(book);
    }

    public void delete(Long id) {
        Book book = getById(id);
        bookRepository.delete(book);
    }

    private Book getById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Genre was not found for id=%d", id)
                ));
    }
}
