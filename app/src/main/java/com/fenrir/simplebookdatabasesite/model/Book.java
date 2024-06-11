package com.fenrir.simplebookdatabasesite.model;

import com.fenrir.simplebookdatabasesite.model.baseentity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "book")
public class Book extends BaseEntity {
    @NotBlank
    @Pattern(regexp = "[0-9]{13}")
    @Column(
            name = "isbn",
            nullable = false,
            unique = true,
            length = 13
    )
    private String isbn;

    @NotBlank
    @Size(min = 3, max = 255)
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "published", nullable = false)
    private LocalDate published;

    @NotNull
    @Min(value = 0)
    @Column(name = "pages", nullable = false, length = 5)
    private Integer pages;

    @Size
    @Column(name = "description", length = 5000)
    private String description;

    @Lob
    @Column(name = "cover")
    private byte[] cover;

    @Setter(AccessLevel.NONE)
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Setter(AccessLevel.NONE)
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDate createdAt;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Author author;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    public Book(
            Long id,
            String isbn,
            String title,
            LocalDate published,
            Integer pages,
            String description,
            byte[] cover,
            Author author,
            Genre genre) {

        super(id);
        this.isbn = isbn;
        this.title = title;
        this.published = published;
        this.pages = pages;
        this.description = description;
        this.cover = cover;
        this.author = author;
        this.genre = genre;
    }
}
