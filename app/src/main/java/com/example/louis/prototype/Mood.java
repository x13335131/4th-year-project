package com.example.louis.prototype;

/**
 * Created by louis on 03/12/2017.
 */

public class Mood {


    String moodID;
    Boolean checkBox1;
    Boolean checkBox2;
    Boolean checkBox3;
    Boolean checkBox4;
    Boolean checkBox5;
    Boolean checkBox6;
    Boolean checkBox7;
    String symptomDate;
    String userID;

    public Mood(){

    }

    public Mood(String moodID, Boolean checkBox1, Boolean checkBox2, Boolean checkBox3, Boolean checkBox4, Boolean checkBox5, Boolean checkBox6, Boolean checkBox7, String symptomDate, String userID) {
        this.moodID = moodID;
        this.checkBox1 = checkBox1;
        this.checkBox2 = checkBox2;
        this.checkBox3 = checkBox3;
        this.checkBox4 = checkBox4;
        this.checkBox5 = checkBox5;
        this.checkBox6 = checkBox6;
        this.checkBox7 = checkBox7;
        this.symptomDate = symptomDate;
        this.userID = userID;
    }

    public String getMoodID() {
        return moodID;
    }

    public Boolean getCheckBox1() {
        return checkBox1;
    }

    public Boolean getCheckBox2() {
        return checkBox2;
    }

    public Boolean getCheckBox3() {
        return checkBox3;
    }

    public Boolean getCheckBox4() {
        return checkBox4;
    }

    public Boolean getCheckBox5() {
        return checkBox5;
    }

    public Boolean getCheckBox6() {
        return checkBox6;
    }

    public Boolean getCheckBox7() {
        return checkBox7;
    }

    public String getSymptomDate() {
        return symptomDate;
    }

    public String getUserID() {
        return userID;
    }
}
