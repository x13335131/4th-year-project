package com.example.louis.prototype;

import java.util.Date;

/**
 * Created by louis on 25/06/2018.
 */

public class ForumPost {

    public String post, userID;
    //public Date timestamp;

  // public Timestamp timestamp;

    public ForumPost(){}
//, Date timestamp
    public ForumPost(String post, String userID) {
        this.post = post;
        this.userID = userID;
      //  this.timestamp = timestamp;
       // this.timestamp = timestamp;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

   /* public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

*/
}
