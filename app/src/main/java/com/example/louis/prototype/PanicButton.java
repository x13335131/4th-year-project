package com.example.louis.prototype;

/**
 * Created by louis on 01/02/2018.
 */

public class PanicButton {
    int length;
    String panicDate;
    double latitude, longitude;
    String location;
    String userID;

    public PanicButton() {

    }
    public PanicButton(int length, String panicDate, double latitude, double longitude, String location, String userID) {
        this.length = length;
        this.panicDate = panicDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.userID = userID;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getPanicDate() {
        return panicDate;
    }

    public void setPanicDate(String panicDate) {
        this.panicDate = panicDate;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}