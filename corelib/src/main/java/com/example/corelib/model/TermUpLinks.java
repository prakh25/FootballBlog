
package com.example.corelib.model;

import com.google.gson.annotations.SerializedName;

public class TermUpLinks {

    @SerializedName("embeddable")
    private Boolean embeddable;
    @SerializedName("href")
    private String href;

    public Boolean getEmbeddable() {
        return embeddable;
    }

    public void setEmbeddable(Boolean embeddable) {
        this.embeddable = embeddable;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

}
