package com.example.corelib.model.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by prakh on 04-12-2017.
 */

public class RegisterUserWithEmail {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("cookie")
    @Expose
    private String cookie;
    @SerializedName("user_id")
    @Expose
    private String userId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
