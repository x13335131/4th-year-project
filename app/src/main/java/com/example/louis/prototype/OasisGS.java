package com.example.louis.prototype;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by louis on 25/02/2018.
 */

public class OasisGS {
    private int totalOasisValue;
    private String todaysDate;
    private String user;
    public OasisGS(){

    }


    public OasisGS(int totalOasisValue, String oasisDate, String uID) {

        this.totalOasisValue = totalOasisValue;
        this.user= uID;
        this.todaysDate = oasisDate;
    }
    public int getTotalOasisValue() {
        return totalOasisValue;
    }

    public String getTodaysDate() {
        return todaysDate;
    }

    public String getUser() {
        return user;
    }
}
