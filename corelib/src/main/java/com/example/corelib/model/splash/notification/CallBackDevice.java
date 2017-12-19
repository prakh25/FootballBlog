package com.example.corelib.model.splash.notification;

import com.google.gson.annotations.Expose;

/**
 * Created by prakh on 18-12-2017.
 */

public class CallBackDevice {
    @Expose
    private String status = "";
    @Expose
    private String message = "";

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
