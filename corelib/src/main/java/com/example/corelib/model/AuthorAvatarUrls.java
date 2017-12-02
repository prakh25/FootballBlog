
package com.example.corelib.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthorAvatarUrls {

    @SerializedName("24")
    private String size_24;
    @SerializedName("48")
    private String size_48;
    @SerializedName("96")
    @Expose
    private String size_96;

    public String get24() {
        return size_24;
    }

    public void set24(String _24) {
        this.size_24 = _24;
    }

    public String get48() {
        return size_48;
    }

    public void set48(String _48) {
        this.size_48 = _48;
    }

    public String get96() {
        return size_96;
    }

    public void set96(String _96) {
        this.size_96 = _96;
    }

}
