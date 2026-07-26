package com.project.tms.commonService.dto;

import java.util.Date;

public class AuthResponseDTO {

    private String token;
    private Date expiresAt;
    private Long id;
    private String username;
    private String role;

    public AuthResponseDTO(String token, Date expiresAt, Long id, String username, String role) {
        this.token = token;
        this.expiresAt = expiresAt;
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}