package com.fenrir.simplebookdatabasesite.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    ROLE_USER,
    ROLE_MODERATOR,
    ROLE_ADMIN;
}
