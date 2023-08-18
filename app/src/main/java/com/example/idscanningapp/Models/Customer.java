package com.example.idscanningapp.Models;

import android.media.Image;

import java.util.Date;
import java.util.UUID;

public class Customer {
    private UUID CustomerID;
    private String FirstName;
    private String MiddleName;
    private String LastName;
    private Date DOB;
    private Date ISS;
    private Date EXP;
    private String DLN;
    private String DD;
    private String Street;
    private String City;
    private String State;
    private String Country;
    private String Zip;
    private char Sex;
    private String EyeColor;
    private String HairColor;
    private String Height;
    private String Weight;
    private Image IDFront;
    private Image IDBack;


    public UUID getCustomerID() { return CustomerID; }
    public void setCustomerID(UUID customerID) { CustomerID = customerID; }

    public String getFirstName() { return FirstName; }
    public void setFirstName(String firstName) { FirstName = firstName; }

    public String getMiddleName() { return MiddleName; }
    public void setMiddleName(String middleName) { MiddleName = middleName; }

    public String getLastName() { return LastName; }
    public void setLastName(String lastName) { LastName = lastName; }

    public Date getDOB() { return DOB; }
    public void setDOB(Date dob) { DOB = dob; }

    public Date getISS() { return ISS; }
    public void setISS(Date iss) { ISS = iss; }

    public Date getEXP() { return EXP; }
    public void setEXP(Date exp) { EXP = exp; }

    public String getDLN() { return DLN; }
    public void setDLN(String dln) { DLN = dln; }

    public String getDD() { return DD; }
    public void setDD(String dd) { DD = dd; }

    public String getStreet() { return Street; }
    public void setStreet(String street) { Street = street; }

    public String getCity() { return City; }
    public void setCity(String city) { City = city; }

    public String getState() { return State; }
    public void setState(String state) { State = state; }

    public String getCountry() { return Country; }
    public void setCountry(String country) { Country = country; }

    public String getZip() {return Zip; }
    public void setZip(String zip) { Zip = zip; }

    public char getSex() { return Sex; }
    public void setSex(char sex) { Sex = sex; }

    public String getEyeColor() { return EyeColor; }
    public void setEyeColor(String eyeColor) { EyeColor = eyeColor; }

    public String getHairColor() { return HairColor; }
    public void setHairColor(String hairColor) { HairColor = hairColor; }

    public String getHeight() { return Height; }
    public void setHeight(String height) { Height = height; }

    public String getWeight() { return Weight; }
    public void setWeight(String weight) { Weight = weight; }

    public Image getIDFront() { return IDFront; }
    public void setIDFront(Image idFront) { IDFront = idFront; }

    public Image getIDBack() { return IDBack; }
    public void setIDBack(Image idBack) { IDBack = idBack; }


}
