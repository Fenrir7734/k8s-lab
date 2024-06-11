package com.fenrir.simplebookdatabasesite.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fenrir.simplebookdatabasesite.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {
    private Long id;
    private String username;
    @JsonIgnore private String email;
    @JsonIgnore private String password;
    private GrantedAuthority authority;
    private Boolean verified;
    private Boolean banned;

    public UserDetailsImpl(User user) {
        this.username = user.getCredentials().getUsername();
        this.email = user.getContact().getEmail();
        this.password = user.getCredentials().getPassword();
        this.authority = new SimpleGrantedAuthority(user.getRole().name());
        this.verified = user.getVerified();
        this.banned = user.getBanned();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !banned;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return verified;
    }
}
