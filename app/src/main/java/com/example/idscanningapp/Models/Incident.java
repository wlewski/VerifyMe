package com.example.idscanningapp.Models;

import androidx.annotation.NonNull;

import java.util.UUID;

public class Incident {
    private UUID ID;
    private String Name;

    public UUID getID() { return ID; }
    public void setID(UUID id) { ID = id; }

    public String getName() { return Name; }
    public void setName(String name) { Name = name; }

    public Incident() { }
    public Incident(UUID id, String name){
        ID = id;
        Name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return Name;
    }

}
