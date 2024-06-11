package com.fenrir.simplebookdatabasesite.controller;

import com.fenrir.simplebookdatabasesite.model.Role;
import com.fenrir.simplebookdatabasesite.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.fenrir.simplebookdatabasesite.common.Constants.ALLOWED_ORIGIN;
import static com.fenrir.simplebookdatabasesite.common.Constants.APPLICATION_HAL_JSON;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = ALLOWED_ORIGIN)
@RequestMapping(
        path = "/api/admin",
        produces = APPLICATION_HAL_JSON
)
public class AdminController {
    private final UserService userService;

    @GetMapping(path = "/{username}/role")
    public ResponseEntity<?> getUserRole(@PathVariable("username") String username) {
        Role role = userService.getUserRole(username);
        return ResponseEntity.ok(role);
    }

    @GetMapping(path = "/roles")
    public ResponseEntity<?> getAllRoles() {
        return ResponseEntity.ok(Role.values());
    }

    @PatchMapping(path = "/{username}/grant/{role}")
    public ResponseEntity<?> grantRole(@PathVariable("username") String username, @PathVariable("role") Role role) {
        userService.updateRole(username, role);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/{username}/block")
    public ResponseEntity<?> blockUser(@PathVariable("username") String username) {
        userService.block(username);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/{username}/unblock")
    public ResponseEntity<?> unblockUser(@PathVariable("username") String username) {
        userService.unblock(username);
        return ResponseEntity.noContent().build();
    }
}
