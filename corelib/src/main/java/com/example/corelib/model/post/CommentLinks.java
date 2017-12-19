
package com.example.corelib.model.post;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommentLinks {

    @SerializedName("self")
    private List<Self> self = null;
    @SerializedName("collection")
    private List<Collection> collection = null;
    @SerializedName("up")
    private List<Up> up = null;
    @SerializedName("author")
    private List<CommentAuthor> author = null;

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

    public List<Up> getUp() {
        return up;
    }

    public void setUp(List<Up> up) {
        this.up = up;
    }

    public List<CommentAuthor> getAuthor() {
        return author;
    }

    public void setAuthor(List<CommentAuthor> author) {
        this.author = author;
    }

}
