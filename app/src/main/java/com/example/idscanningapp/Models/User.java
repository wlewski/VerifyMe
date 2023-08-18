package com.example.idscanningapp.Models;

import java.util.UUID;

public class User {
    private UUID ID;
    private UUID UserRoleID;
    private String Username;
    private String Password;
    private String FirstName;
    private String LastName;

    public UUID getID() { return ID; }
    public void setID(UUID id) { ID = id; }

    public UUID getUserRoleID() { return UserRoleID; }
    public void setUserRoleID(UUID userRoleID) { UserRoleID = userRoleID; }

    public String getUsername() { return Username; }
    public void setUsername(String username) { Username = username; }

    public String getPassword() { return Password; }
    public void setPassword(String password) { Password = password; }

    public String getFirstName() { return FirstName; }
    public void setFirstName(String firstName) { FirstName = firstName; }

    public String getLastName() { return LastName; }
    public void setLastName(String lastName) { LastName = lastName; }
}
