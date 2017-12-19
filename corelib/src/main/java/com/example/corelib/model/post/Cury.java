
package com.example.corelib.model.post;

import com.google.gson.annotations.SerializedName;

public class Cury {

    @SerializedName("name")
    private String name;
    @SerializedName("href")
    private String href;
    @SerializedName("templated")
    private Boolean templated;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Boolean getTemplated() {
        return templated;
    }

    public void setTemplated(Boolean templated) {
        this.templated = templated;
    }

}
