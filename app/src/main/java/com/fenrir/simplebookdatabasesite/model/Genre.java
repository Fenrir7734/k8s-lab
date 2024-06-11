package com.fenrir.simplebookdatabasesite.model;

import com.fenrir.simplebookdatabasesite.model.baseentity.UniquelyNamedEntity;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@Entity
@Table(name = "genre")
public class Genre extends UniquelyNamedEntity {
    public Genre(Long id, String name) {
        super(id, name);
    }
}
