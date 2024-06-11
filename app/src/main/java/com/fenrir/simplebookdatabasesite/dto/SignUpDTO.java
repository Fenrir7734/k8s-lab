package com.fenrir.simplebookdatabasesite.dto;

import com.fenrir.simplebookdatabasesite.common.validator.Password;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
public class SignUpDTO {

    @NotBlank
    @Size(min = 4, max = 30)
    private String username;

    @Size(min = 2, max = 50)
    private String firstname;

    @Size(min = 2, max = 50)
    private String lastname;

    @Email
    private String email;

    @Pattern(regexp = "[0-9]{11}")
    private String phone;

    @Password
    private String password;
}
