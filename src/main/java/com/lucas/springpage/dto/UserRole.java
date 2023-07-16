package com.lucas.springpage.dto;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");


    UserRole(String roleUser) {
        this.value = roleUser;
    }
    private String value;
}
