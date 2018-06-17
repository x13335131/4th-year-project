package com.example.louis.prototype;

/**
 * Created by louis on 25/02/2018.
 */

public class OdsisGS {
    private int totalOdsisValue;
    private String todaysDate;
    private String user;
    public OdsisGS(){

    }


    public OdsisGS(int totalOdsisValue, String oasisDate, String uID) {

        this.totalOdsisValue = totalOdsisValue;
        this.user= uID;
        this.todaysDate = oasisDate;
    }
    public int getTotalOdsisValue() {
        return totalOdsisValue;
    }

    public String getTodaysDate() {
        return todaysDate;
    }

    public String getUser() {
        return user;
    }
}
