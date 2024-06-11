package com.fenrir.simplebookdatabasesite.model;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "shelf")
public class Shelf {
    @Setter(AccessLevel.NONE)
    @EmbeddedId
    private Id id = new Id();

    @Size(min = 3, max = 1000)
    @Column(name = "review", length = 1000)
    private String content;

    @NotNull
    @Min(1)
    @Max(5)
    @Column(name = "rate", nullable = false)
    private Integer rate;

    @Setter(AccessLevel.NONE)
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Setter(AccessLevel.NONE)
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(
            name = "book_id",
            insertable = false, updatable = false
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Book book;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            insertable = false, updatable = false
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public Shelf(Id id, String content, Integer rate) {
        this.id = id;
        this.content = content;
        this.rate = rate;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode
    @Embeddable
    public static class Id implements Serializable {
        @Column(name = "user_id")
        private Long userId;

        @Column(name = "book_id")
        private Long bookId;
    }
}
