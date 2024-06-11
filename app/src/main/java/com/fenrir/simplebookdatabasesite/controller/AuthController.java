package com.fenrir.simplebookdatabasesite.controller;

import com.fenrir.simplebookdatabasesite.dto.JwtTokenDTO;
import com.fenrir.simplebookdatabasesite.dto.SignInDTO;
import com.fenrir.simplebookdatabasesite.dto.SignUpDTO;
import com.fenrir.simplebookdatabasesite.dto.UserDTO;
import com.fenrir.simplebookdatabasesite.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static com.fenrir.simplebookdatabasesite.common.Constants.ALLOWED_ORIGIN;
import static com.fenrir.simplebookdatabasesite.common.Constants.APPLICATION_HAL_JSON;


@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = ALLOWED_ORIGIN)
@RequestMapping(
        path = "/api/auth",
        produces = APPLICATION_HAL_JSON
)
public class AuthController {
    private final UserService userService;

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signup(@RequestBody @Valid SignUpDTO signUpDTO, UriComponentsBuilder builder) {
        UserDTO user = userService.registerUser(signUpDTO);
        String username = user.getUsername();
        URI location = builder.replacePath("/api/users/{username}")
                .buildAndExpand(username)
                .toUri();
        return ResponseEntity.created(location).body(user);
    }

    @PostMapping(value = "/signin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signIn(@RequestBody @Valid SignInDTO signInDTO) {
        JwtTokenDTO token = userService.authenticateUser(signInDTO);
        return ResponseEntity.ok(token);
    }

    @PostMapping(value = "/valid", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> validateToken(@RequestBody @Valid JwtTokenDTO jwtTokenDTO) {
        return userService.validateToken(jwtTokenDTO)
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
