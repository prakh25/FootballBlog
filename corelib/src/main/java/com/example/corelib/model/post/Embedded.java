
package com.example.corelib.model.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Embedded {

    @SerializedName("author")
    @Expose
    private List<Author> author = null;
    @SerializedName("replies")
    @Expose
    private List<List<Comment>> comments = null;
    @SerializedName("wp:featuredmedia")
    @Expose
    private FeaturedMedia featuredMedia;
    @SerializedName("wp:term")
    @Expose
    private List<List<Terms>> wpTerm = null;

    public List<Author> getAuthor() {
        return author;
    }

    public void setAuthor(List<Author> author) {
        this.author = author;
    }

    public List<List<Comment>> getComments() {
        return comments;
    }

    public void setComments(List<List<Comment>> comments) {
        this.comments = comments;
    }

    public FeaturedMedia getFeaturedMedia() {
        return featuredMedia;
    }

    public void setFeaturedMedia(FeaturedMedia featuredMedia) {
        this.featuredMedia = featuredMedia;
    }

    public List<List<Terms>> getWpTerm() {
        return wpTerm;
    }

    public void setWpTerm(List<List<Terms>> wpTerm) {
        this.wpTerm = wpTerm;
    }

}
