package com.example.corelib.model.auth.user_network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by prakh on 29-12-2017.
 */

public class RefreshedCookie {
    @SerializedName("prev_cookie")
    @Expose
    private String prevCookie;
    @SerializedName("new_cookie")
    @Expose
    private String newCookie;
    @SerializedName("cookie_name")
    private String cookieName;
    @SerializedName("expire_in")
    private Long seconds;
    @SerializedName("user")
    @Expose
    private UserData userData;

    public String getPrevCookie() {
        return prevCookie;
    }

    public String getNewCookie() {
        return newCookie;
    }

    public String getCookieName() {
        return cookieName;
    }

    public Long getSeconds() {
        return seconds;
    }

    public UserData getUserData() {
        return userData;
    }
}
