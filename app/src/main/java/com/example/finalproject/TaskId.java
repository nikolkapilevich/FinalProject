package com.example.finalproject;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class TaskId {
    @Exclude
    public String Id;
    public <T extends TaskId> T withId (@NonNull final String id){
        this.Id = id;
        return (T) this;
    }
}
