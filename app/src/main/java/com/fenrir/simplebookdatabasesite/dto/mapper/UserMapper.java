package com.fenrir.simplebookdatabasesite.dto.mapper;

import com.fenrir.simplebookdatabasesite.dto.JwtTokenDTO;
import com.fenrir.simplebookdatabasesite.dto.SignUpDTO;
import com.fenrir.simplebookdatabasesite.dto.UserDTO;
import com.fenrir.simplebookdatabasesite.dto.UserSlimDTO;
import com.fenrir.simplebookdatabasesite.model.Contact;
import com.fenrir.simplebookdatabasesite.model.Credentials;
import com.fenrir.simplebookdatabasesite.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserSlimDTO toUserSlimDTO(User user) {
        return new UserSlimDTO(
                user.getCredentials().getUsername(),
                user.getCreatedAt(),
                user.getBanned()
        );
    }

    public UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.getCredentials().getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.getContact().getEmail(),
                user.getContact().getPhone(),
                user.getVerified(),
                user.getBanned(),
                user.getCreatedAt()
        );
    }

    public JwtTokenDTO toJwtTokenDTO(String token) {
        return new JwtTokenDTO(token);
    }

    public User fromSignupDTO(SignUpDTO signupDTO) {
        Credentials credentials = new Credentials(signupDTO.getUsername(), signupDTO.getPassword());
        Contact contact = new Contact(signupDTO.getEmail(), signupDTO.getPhone());
        return new User(
                signupDTO.getFirstname(),
                signupDTO.getLastname(),
                credentials,
                contact,
                true,
                false
        );
    }
}
