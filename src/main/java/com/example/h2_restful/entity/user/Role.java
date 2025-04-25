package com.example.h2_restful.entity.user;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_USER("User"),
    ROLE_ADMIN("Admin");

    private String displayValue;
    Role(String displayValue) {
        this.displayValue = displayValue;
    }

}
