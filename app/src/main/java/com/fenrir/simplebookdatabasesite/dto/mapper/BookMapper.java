package com.fenrir.simplebookdatabasesite.dto.mapper;

import com.fenrir.simplebookdatabasesite.dto.BookDTO;
import com.fenrir.simplebookdatabasesite.dto.BookSlimDTO;
import com.fenrir.simplebookdatabasesite.model.Author;
import com.fenrir.simplebookdatabasesite.model.Book;
import com.fenrir.simplebookdatabasesite.model.Genre;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {
    public BookDTO toBookDTO(Book book) {
        return new BookDTO(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getPublished(),
                book.getPages(),
                book.getDescription(),
                book.getAuthor().getId(),
                book.getGenre().getId()
        );
    }

    public BookSlimDTO toBookSlimDTO(Book book) {
        return new BookSlimDTO(
                book.getId(),
                book.getTitle(),
                book.getDescription(),
                book.getCover(),
                book.getAuthor().getId(),
                book.getAuthor().getFullName()
        );
    }

    public Book fromBookDTO(BookDTO book) {
        Author author = new Author();
        author.setId(book.getAuthorId());

        Genre genre = new Genre();
        genre.setId(book.getGenreId());

        return new Book(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getPublished(),
                book.getPages(),
                book.getDescription(),
                null,
                author,
                genre
        );
    }
}
