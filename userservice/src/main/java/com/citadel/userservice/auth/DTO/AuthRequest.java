package com.citadel.userservice.auth.DTO;

import lombok.Data;

import java.util.List;

@Data
public class AuthRequest {
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

