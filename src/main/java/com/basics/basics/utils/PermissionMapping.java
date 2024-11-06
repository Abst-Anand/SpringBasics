package com.basics.basics.utils;

import com.basics.basics.entities.enums.Permissions;
import com.basics.basics.entities.enums.Roles;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.basics.basics.entities.enums.Permissions.*;
import static com.basics.basics.entities.enums.Roles.ADMIN;
import static com.basics.basics.entities.enums.Roles.CREATOR;

public class PermissionMapping {
    private static final Map<Roles, Set<Permissions>> map = Map.of(
            Roles.USER, Set.of(USER_VIEW, POST_VIEW),
            CREATOR, Set.of(USER_UPDATE, POST_CREATE, POST_UPDATE),
            ADMIN, Set.of(USER_CREATE, USER_DELETE, POST_DELETE,  USER_UPDATE, POST_CREATE, POST_UPDATE)
    );

    public static Set<SimpleGrantedAuthority> getAuthoritiesForRole(Roles role) {

        Set<SimpleGrantedAuthority> authorities = map.get(role).stream().map(permission -> new SimpleGrantedAuthority(permission.name())).collect(Collectors.toSet());

        return authorities;
    }
}
