package com.fenrir.simplebookdatabasesite.model.baseentity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
public class UniquelyNamedEntity extends BaseEntity {
    @NotBlank
    @Size(min = 3, max = 45)
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public UniquelyNamedEntity(Long id, String name) {
        super(id);
        this.name = name;
    }
}
