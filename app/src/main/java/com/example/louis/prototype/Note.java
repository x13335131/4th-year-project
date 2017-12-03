package com.example.louis.prototype;

/**
 * Created by louis on 03/12/2017.
 */

public class Note {
    String noteID;
    String note;
    String noteDate;
    String userID;

    public Note(){//used while reading the values

    }



    public Note(String noteID, String note, String noteDate, String userID) {
        this.noteID = noteID;
        this.note = note;
        this.noteDate = noteDate;
        this.userID = userID;
    }

    public String getNoteID() {
        return noteID;
    }

    public String getNote() {
        return note;
    }

    public String getNoteDate() {
        return noteDate;
    }
    public String getUserID() {
        return userID;
    }
}
