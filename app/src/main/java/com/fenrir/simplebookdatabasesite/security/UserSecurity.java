package com.fenrir.simplebookdatabasesite.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("userSecurity")
public class UserSecurity {
    public boolean isResourceOwner(Authentication authentication, String username) {
        if (authentication.getPrincipal() instanceof UserDetailsImpl principal) {
            return principal.getUsername().equals(username);
        }
        return false;
    }
}
