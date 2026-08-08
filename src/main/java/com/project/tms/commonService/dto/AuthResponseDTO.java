package com.project.tms.commonService.dto;

import java.util.Date;
import java.util.UUID;

public class AuthResponseDTO {

    private String token;
    private Date expiresAt;
    private UUID id;
    private String userName;
    private String role;

    public AuthResponseDTO(String token, Date expiresAt, UUID id, String userName, String role) {
        this.token = token;
        this.expiresAt = expiresAt;
        this.id = id;
        this.userName = userName;
        this.role = role;
    }

    public AuthResponseDTO(UUID id, String userName, String role) {
        this.id = id;
        this.userName = userName;
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}