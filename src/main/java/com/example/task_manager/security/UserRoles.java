package com.example.task_manager.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.task_manager.security.UserPermissions.*;

@RequiredArgsConstructor
@Getter
public enum UserRoles {
    USER(Set.of(TASK_READ)),
    ADMIN(Set.of(TASK_READ, TASK_WRITE, USER_READ, USER_WRITE));

    private final Set<UserPermissions> permissions;

    public Set<SimpleGrantedAuthority> getGrantedAuthority() {
        Set<SimpleGrantedAuthority> authorities = getPermissions().stream()
                .map(permission ->new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
