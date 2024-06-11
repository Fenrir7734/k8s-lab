package com.fenrir.simplebookdatabasesite.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class Contact {
    @NotBlank
    @Email
    @Column(name = "email", nullable = false)
    private String email;

    @Pattern(regexp = "[0-9]{11}")
    @Column(name = "phone", length = 11)
    private String phone;

    public Contact(String email, String phone) {
        this.email = email;
        this.phone = phone;
    }
}
