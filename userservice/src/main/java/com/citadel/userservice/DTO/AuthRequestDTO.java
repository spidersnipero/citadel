package com.citadel.userservice.DTO;

import lombok.Data;

import java.util.List;

@Data
public class AuthRequestDTO {
    private String email;
    private String password;
    private String name;
    private List<String> roles;


    @Override
    public String toString() {
        return "AuthRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", roles=" + roles +
                '}';
    }
}

