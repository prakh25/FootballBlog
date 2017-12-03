package com.example.corelib.model.auth.usernamevalidator;

import com.google.gson.annotations.Expose;

/**
 * Created by prakh on 03-12-2017.
 */

public class UsernameExists {
    @Expose
    private String status;
    @Expose
    private boolean username_exists;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isUsername_exists() {
        return username_exists;
    }

    public void setUsername_exists(boolean username_exists) {
        this.username_exists = username_exists;
    }
}
