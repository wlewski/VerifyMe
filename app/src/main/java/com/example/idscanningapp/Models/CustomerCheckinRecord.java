package com.example.idscanningapp.Models;

import java.util.Date;
import java.util.UUID;

public class CustomerCheckinRecord {
    private UUID ID;
    private UUID CustomerID;
    private String CustomerInitials;
    private Date CheckinDate;

    public UUID getID() { return ID; }
    public void setID(UUID id) { ID = id; }

    public UUID getCustomerID() { return CustomerID; }
    public void setCustomerID(UUID customerID) { CustomerID = customerID; }

    public String getCustomerInitials() { return CustomerInitials; }
    public void setCustomerInitials(String customerInitials) { CustomerInitials = customerInitials; }

    public Date getCheckinDate() { return CheckinDate; }
    public void setCheckinDate(Date checkinDate) {CheckinDate = checkinDate; }
}
