package com.reddit.redditcloneback.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleType {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String name;
}
