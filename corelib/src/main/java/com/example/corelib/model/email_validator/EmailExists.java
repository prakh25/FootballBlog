package com.example.corelib.model.email_validator;

import com.google.gson.annotations.Expose;

/**
 * Created by prakh on 03-12-2017.
 */

public class EmailExists {

    @Expose
    private String status;
    @Expose
    private boolean email_exists;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isEmail_exists() {
        return email_exists;
    }

    public void setEmail_exists(boolean email_exists) {
        this.email_exists = email_exists;
    }
}
