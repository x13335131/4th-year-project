package com.example.louis.prototype;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

/**
 * Created by louis on 30/07/2018.
 */


public class ForumPostId {
    @Exclude
    public String ForumPostId;

    public <T extends ForumPostId> T withId(@NonNull final String id) {
        this.ForumPostId = id;
        return (T) this;
    }
}


