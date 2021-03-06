
package com.example.corelib.model.post;

import com.google.gson.annotations.SerializedName;

public class Up {

    @SerializedName("embeddable")
    private Boolean embeddable;
    @SerializedName("post_type")
    private String postType;
    @SerializedName("href")
    private String href;

    public Boolean getEmbeddable() {
        return embeddable;
    }

    public void setEmbeddable(Boolean embeddable) {
        this.embeddable = embeddable;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

}
