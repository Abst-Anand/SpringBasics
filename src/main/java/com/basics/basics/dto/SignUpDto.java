package com.basics.basics.dto;

import com.basics.basics.entities.enums.Permissions;
import com.basics.basics.entities.enums.Roles;
import lombok.Data;

import java.util.Set;

@Data
public class SignUpDto {
    private String email;
    private String password;
    private String name;
    private Set<Roles> roles;
    private Set<Permissions> permissions;
}
