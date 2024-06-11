package com.fenrir.simplebookdatabasesite.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class Credentials {
    @NotBlank
    @Size(min = 4, max = 30)
    @Column(
            name = "username",
            nullable = false,
            unique = true
    )
    private String username;

    @NotBlank
    @Size(min = 60, max = 60)
    @Column(
            name = "password",
            nullable = false,
            length = 60
    )
    private String password;

    public Credentials(String login, String password) {
        this.username = login;
        this.password = password;
    }
}
