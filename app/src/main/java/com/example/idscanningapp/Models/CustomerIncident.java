package com.example.idscanningapp.Models;

import java.util.Date;
import java.util.UUID;

public class CustomerIncident {
    private UUID ID;
    private UUID CustomerID;
    private String CustomerFullName;
    private UUID UserID;
    private String UserInitials;
    private UUID IncidentID;
    private String IncidentName;
    private Date IncidentDate;
    private int FlagLevel;
    private String AdditionalInfo;

    public UUID getID() { return ID; }
    public void setID(UUID id) { ID = id; }

    public UUID getCustomerID() { return CustomerID; }
    public void setCustomerID(UUID customerID) { CustomerID = customerID; }

    public String getCustomerFullName() { return CustomerFullName; }
    public void setCustomerFullName(String customerFullName) { CustomerFullName = customerFullName; }

    public UUID getUserID() { return UserID; }
    public void setUserID(UUID userID) { UserID = userID; }

    public String getUserInitials() { return UserInitials; }
    public void setUserInitials(String userInitials) { UserInitials = userInitials; }

    public UUID getIncidentID() { return IncidentID; }
    public void setIncidentID(UUID incidentID) { IncidentID = incidentID; }

    public String getIncidentName() { return IncidentName; }
    public void setIncidentName(String incidentName) { IncidentName = incidentName; }

    public Date getDate() { return IncidentDate; }
    public void setIncidentDate(Date incidentDate) { IncidentDate = incidentDate; }

    public int getFlagLevel() { return FlagLevel; }
    public void setFlagLevel(int flagLevel) { FlagLevel = flagLevel; }

    public String getAdditionalInfo() { return AdditionalInfo; }
    public void setAdditionalInfo(String additionalInfo) { AdditionalInfo = additionalInfo; }
}
