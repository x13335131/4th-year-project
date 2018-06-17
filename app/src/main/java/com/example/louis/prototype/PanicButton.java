package com.example.louis.prototype;

/**
 * Created by louis on 01/02/2018.
 */

public class PanicButton {
    int length;
    String panicDate;
    String userID;


    public PanicButton() {

    }
    public PanicButton(int length, String panicDate, String userID) {
        this.length = length;
        this.panicDate = panicDate;
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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}