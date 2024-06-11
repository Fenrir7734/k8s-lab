package com.fenrir.simplebookdatabasesite.controller;

import com.fenrir.simplebookdatabasesite.assembler.UserModelAssembler;
import com.fenrir.simplebookdatabasesite.assembler.UserSlimModelAssembler;
import com.fenrir.simplebookdatabasesite.dto.NewPasswordDTO;
import com.fenrir.simplebookdatabasesite.dto.UserDTO;
import com.fenrir.simplebookdatabasesite.dto.UserSlimDTO;
import com.fenrir.simplebookdatabasesite.dto.UserUpdateDTO;
import com.fenrir.simplebookdatabasesite.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.fenrir.simplebookdatabasesite.common.Constants.ALLOWED_ORIGIN;
import static com.fenrir.simplebookdatabasesite.common.Constants.APPLICATION_HAL_JSON;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = ALLOWED_ORIGIN)
@RequestMapping(
        path = "/api/users",
        produces = APPLICATION_HAL_JSON
)
public class UserController {
    private final UserService userService;
    private final UserModelAssembler userAssembler;
    private final UserSlimModelAssembler userSlimAssembler;
    private final PagedResourcesAssembler<UserSlimDTO> pagedAssembler;

    @GetMapping(path = "/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable("username") String username) {
        UserSlimDTO user = userService.get(username);
        return ResponseEntity.ok(userSlimAssembler.toModel(user));
    }

    @GetMapping(path = "/id/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") Long userId) {
        UserSlimDTO user = userService.get(userId);
        return ResponseEntity.ok(userSlimAssembler.toModel(user));
    }

    @GetMapping(path = "/{username}/info")
    public ResponseEntity<?> getUserInfoByUsername(@PathVariable("username") String username) {
        UserDTO user = userService.getUserDetails(username);
        return ResponseEntity.ok(userAssembler.toModel(user));
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<UserSlimDTO> users = userService.getAll(pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(users, userSlimAssembler));
    }

    @PutMapping(path = "/{username}")
    public ResponseEntity<?> updateUser(
            @PathVariable("username") String username,
            @RequestBody UserUpdateDTO userDTO) {

        UserDTO user = userService.update(username, userDTO);
        return ResponseEntity.ok(userAssembler.toModel(user));
    }

    @PutMapping(path = "/{username}/change-pass")
    public ResponseEntity<?> changePassword(
            @PathVariable("username") String username,
            @RequestBody NewPasswordDTO passwordDTO) {

        userService.updatePassword(username, passwordDTO);
        return ResponseEntity.noContent().build();
    }
}
