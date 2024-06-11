package com.fenrir.simplebookdatabasesite.model;

import com.fenrir.simplebookdatabasesite.model.baseentity.PersonEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "author")
public class Author extends PersonEntity {
    @Size(max = 1000)
    @Column(name = "description")
    private String description;

    @NotNull
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "death_date")
    private LocalDate deathDate;

    @Setter(AccessLevel.NONE)
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Setter(AccessLevel.NONE)
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Author(
            Long id,
            String firstname,
            String lastname,
            String description,
            LocalDate birthDate,
            LocalDate deathDate) {

        super(id, firstname, lastname);
        this.description = description;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
    }
}
