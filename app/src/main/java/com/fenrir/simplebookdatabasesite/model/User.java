package com.fenrir.simplebookdatabasesite.model;


import com.fenrir.simplebookdatabasesite.model.baseentity.PersonEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
public class User extends PersonEntity {
    @Embedded
    private Credentials credentials;

    @Embedded
    private Contact contact;

    @Column(name = "verified", nullable = false)
    private Boolean verified;

    @Column(name = "banned", nullable = false)
    private Boolean banned;

    @Setter(AccessLevel.NONE)
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    public User(
            Long id,
            String firstname,
            String lastname,
            Credentials credentials,
            Contact contact,
            Role role) {

        super(id, firstname, lastname);
        this.credentials = credentials;
        this.contact = contact;
        this.role = role;
    }

    public User(
            String firstname,
            String lastname,
            Credentials credentials,
            Contact contact,
            boolean verified,
            boolean banned) {

        super(null, firstname, lastname);
        this.credentials = credentials;
        this.contact = contact;
        this.role = Role.ROLE_USER;
        this.verified = verified;
        this.banned = banned;
    }
}
