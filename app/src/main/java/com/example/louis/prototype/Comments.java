package com.example.louis.prototype;

import java.util.Date;

/**
 * Created by louis on 30/07/2018.
 */

public class Comments {

    private String comment, userID;
    private Date timestamp;

    public Comments() {
    }

    public Comments(String comment, String userID, Date timestamp) {
        this.comment = comment;
        this.userID = userID;
        this.timestamp = timestamp;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
