package com.fenrir.simplebookdatabasesite.service;

import com.fenrir.simplebookdatabasesite.dto.*;
import com.fenrir.simplebookdatabasesite.dto.mapper.UserMapper;
import com.fenrir.simplebookdatabasesite.exception.exceptions.DuplicateCredentialsException;
import com.fenrir.simplebookdatabasesite.exception.exceptions.PasswordMismatchException;
import com.fenrir.simplebookdatabasesite.exception.exceptions.ResourceNotFoundException;
import com.fenrir.simplebookdatabasesite.model.Credentials;
import com.fenrir.simplebookdatabasesite.model.Role;
import com.fenrir.simplebookdatabasesite.model.User;
import com.fenrir.simplebookdatabasesite.repository.UserRepository;
import com.fenrir.simplebookdatabasesite.security.UserDetailsImpl;
import com.fenrir.simplebookdatabasesite.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;

    public UserDTO registerUser(SignUpDTO signupDTO) {
        if (userRepository.existsByContact_Email(signupDTO.getEmail())) {
            throw new DuplicateCredentialsException("Account with this email address already exists.");
        }
        if (userRepository.existsByCredentials_Username(signupDTO.getUsername())) {
            throw new DuplicateCredentialsException("Account with this username already exists.");
        }

        User user = userMapper.fromSignupDTO(signupDTO);
        Credentials credentials = user.getCredentials();
        credentials.setPassword(passwordEncoder.encode(credentials.getPassword()));
        user = userRepository.save(user);
        return userMapper.toUserDTO(user);
    }

    public JwtTokenDTO authenticateUser(SignInDTO signInDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInDTO.getUsername(),
                        signInDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        String token = jwtUtils.generateJwtToken(principal);
        return userMapper.toJwtTokenDTO(token);
    }

    public boolean validateToken(JwtTokenDTO tokenDTO) {
        return jwtUtils.validateToken(tokenDTO.getAccessToken());
    }

    public UserSlimDTO get(String username) {
        User user = getByUsername(username);
        return userMapper.toUserSlimDTO(user);
    }

    public UserSlimDTO get(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("User was not found for id=%d", id)
                ));
        return userMapper.toUserSlimDTO(user);
    }

    public UserDTO getUserDetails(String username) {
        User user = getByUsername(username);
        return userMapper.toUserDTO(user);
    }

    public Role getUserRole(String username) {
        return getByUsername(username).getRole();
    }

    public Page<UserSlimDTO> getAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toUserSlimDTO);
    }

    public UserDTO update(String username, UserUpdateDTO userUpdateDTO) {
        User user = getByUsername(username);
        user.setFirstname(userUpdateDTO.getFirstname());
        user.setLastname(userUpdateDTO.getLastname());
        user.getContact().setPhone(userUpdateDTO.getPhone());
        user = userRepository.save(user);
        return userMapper.toUserDTO(user);
    }

    public void updatePassword(String username, NewPasswordDTO passwordDTO) {
        User user = getByUsername(username);
        Credentials credentials = user.getCredentials();

        if (!passwordEncoder.matches(passwordDTO.getOldPassword(), credentials.getPassword())) {
            throw new PasswordMismatchException("Provided password do not match current password");
        }

        String newPassword = passwordDTO.getNewPassword();
        if (!newPassword.equals(passwordDTO.getNewPasswordConfirmation())) {
            throw new PasswordMismatchException("New password and confirmation password are different");
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);
        credentials.setPassword(encodedNewPassword);
        userRepository.save(user);
    }

    public void updateRole(String username, Role role) {
        User user = getByUsername(username);
        user.setRole(role);
        userRepository.save(user);
    }

    public void block(String username) {
        User user = getByUsername(username);
        user.setBanned(true);
        userRepository.save(user);
    }

    public void unblock(String username) {
        User user = getByUsername(username);
        user.setBanned(false);
        userRepository.save(user);
    }

    public void verify(String username) {
        User user = getByUsername(username);
        user.setVerified(true);
        userRepository.save(user);
    }

    private User getByUsername(String username) {
        return userRepository.findByCredentials_Username(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("User was not found for username=%s", username)
                ));
    }

}
