package com.fenrir.simplebookdatabasesite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class SignInDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
