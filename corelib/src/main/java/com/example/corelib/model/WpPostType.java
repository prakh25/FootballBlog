
package com.example.corelib.model;

import com.google.gson.annotations.SerializedName;

public class WpPostType {

    @SerializedName("href")
    private String href;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

}
