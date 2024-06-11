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
public class PersonEntity extends BaseEntity {
    @NotBlank
    @Size(min = 2, max = 50)
    @Column(name = "firstname", nullable = false)
    private String firstname;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(name = "lastname", nullable = false)
    private String lastname;

    public PersonEntity(Long id, String firstname, String lastname) {
        super(id);
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getFullName() {
        return firstname + " " + lastname;
    }
}
