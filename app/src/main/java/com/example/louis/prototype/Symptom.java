package com.example.louis.prototype;

/**
 * Created by louis on 03/12/2017.
 */

public class Symptom {
    String symptomID;
    int value1;
    int value2;
    int value3;
    int value4;
    int value5;
    int value6;
    int value7;
    int value8;
    String symptomDate;
    String userID;

    public Symptom(){

    }

    public Symptom(String symptomID, int value1, int value2, int value3, int value4, int value5, int value6, int value7, int value8, String symptomDate, String userID) {
        this.symptomID = symptomID;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
        this.value5 = value5;
        this.value6 = value6;
        this.value7 = value7;
        this.value8 = value8;
        this.symptomDate = symptomDate;
        this.userID = userID;
    }

    public String getSymptomID() {
        return symptomID;
    }

    public int getValue1() {
        return value1;
    }

    public int getValue2() {
        return value2;
    }

    public int getValue3() {
        return value3;
    }

    public int getValue4() {
        return value4;
    }

    public int getValue5() {
        return value5;
    }

    public int getValue6() {
        return value6;
    }

    public int getValue7() {
        return value7;
    }

    public int getValue8() {
        return value8;
    }

    public String getSymptomDate() {
        return symptomDate;
    }

    public String getUserID() {
        return userID;
    }
}


