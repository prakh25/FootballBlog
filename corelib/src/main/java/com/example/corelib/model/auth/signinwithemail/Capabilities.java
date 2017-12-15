package com.example.corelib.model.auth.signinwithemail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by prakh on 04-12-2017.
 */

public class Capabilities {

    @SerializedName("author")
    @Expose
    private Boolean author;

    public Boolean getAuthor() {
        return author;
    }

    public void setAuthor(Boolean author) {
        this.author = author;
    }
}
