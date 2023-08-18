package com.example.idscanningapp.Models;

import androidx.annotation.NonNull;

import java.util.UUID;

public class UserRole {
    private UUID ID;
    private String Role;

    public UUID getID() { return ID; }
    public void setID(UUID id) { ID = id; }

    public String getRole() { return Role; }
    public void setRole(String role) { Role = role; }

    public UserRole(UUID id, String role) {
        ID = id;
        Role = role;
    }
    public UserRole() { }
    @NonNull
    @Override
    public String toString() {
        return Role;
    }
}
