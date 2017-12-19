
package com.example.corelib.model.post;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TermLinks {

    @SerializedName("self")
    private List<Self> self = null;
    @SerializedName("collection")
    private List<Collection> collection = null;
    @SerializedName("about")
    private List<About> about = null;
    @SerializedName("up")
    private List<TermUpLinks> up = null;
    @SerializedName("wp:post_type")
    private List<WpPostType> wpPostType = null;
    @SerializedName("curies")
    private List<Cury> curies = null;

    public List<Self> getSelf() {
        return self;
    }

    public void setSelf(List<Self> self) {
        this.self = self;
    }

    public List<Collection> getCollection() {
        return collection;
    }

    public void setCollection(List<Collection> collection) {
        this.collection = collection;
    }

    public List<About> getAbout() {
        return about;
    }

    public void setAbout(List<About> about) {
        this.about = about;
    }

    public List<TermUpLinks> getUp() {
        return up;
    }

    public void setUp(List<TermUpLinks> up) {
        this.up = up;
    }

    public List<WpPostType> getWpPostType() {
        return wpPostType;
    }

    public void setWpPostType(List<WpPostType> wpPostType) {
        this.wpPostType = wpPostType;
    }

    public List<Cury> getCuries() {
        return curies;
    }

    public void setCuries(List<Cury> curies) {
        this.curies = curies;
    }

}
