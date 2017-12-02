
package com.example.corelib.model.related_post;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RelatedPostsList implements Parcelable
{

    @SerializedName("posts")
    @Expose
    private List<RelatedPost> relatedPosts = null;
    @SerializedName("termcount")
    @Expose
    private List<String> termcount = null;
    @SerializedName("post_id")
    @Expose
    private Integer postId;
    @SerializedName("post_types")
    @Expose
    private List<String> postTypes = null;
    @SerializedName("taxonomies")
    @Expose
    private List<String> taxonomies = null;
    @SerializedName("related_terms")
    @Expose
    private List<Integer> relatedTerms = null;
    @SerializedName("rendered")
    @Expose
    private String rendered;
    public final static Creator<RelatedPostsList> CREATOR = new Creator<RelatedPostsList>() {


        @SuppressWarnings({
            "unchecked"
        })
        public RelatedPostsList createFromParcel(Parcel in) {
            return new RelatedPostsList(in);
        }

        public RelatedPostsList[] newArray(int size) {
            return (new RelatedPostsList[size]);
        }

    }
    ;

    protected RelatedPostsList(Parcel in) {
        in.readList(this.relatedPosts, (RelatedPost.class.getClassLoader()));
        in.readList(this.termcount, (String.class.getClassLoader()));
        this.postId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.postTypes, (String.class.getClassLoader()));
        in.readList(this.taxonomies, (String.class.getClassLoader()));
        in.readList(this.relatedTerms, (Integer.class.getClassLoader()));
        this.rendered = ((String) in.readValue((String.class.getClassLoader())));
    }

    public RelatedPostsList() {
    }

    public List<RelatedPost> getRelatedPosts() {
        return relatedPosts;
    }

    public void setRelatedPosts(List<RelatedPost> relatedPosts) {
        this.relatedPosts = relatedPosts;
    }

    public List<String> getTermcount() {
        return termcount;
    }

    public void setTermcount(List<String> termcount) {
        this.termcount = termcount;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public List<String> getPostTypes() {
        return postTypes;
    }

    public void setPostTypes(List<String> postTypes) {
        this.postTypes = postTypes;
    }

    public List<String> getTaxonomies() {
        return taxonomies;
    }

    public void setTaxonomies(List<String> taxonomies) {
        this.taxonomies = taxonomies;
    }

    public List<Integer> getRelatedTerms() {
        return relatedTerms;
    }

    public void setRelatedTerms(List<Integer> relatedTerms) {
        this.relatedTerms = relatedTerms;
    }

    public String getRendered() {
        return rendered;
    }

    public void setRendered(String rendered) {
        this.rendered = rendered;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(relatedPosts);
        dest.writeList(termcount);
        dest.writeValue(postId);
        dest.writeList(postTypes);
        dest.writeList(taxonomies);
        dest.writeList(relatedTerms);
        dest.writeValue(rendered);
    }

    public int describeContents() {
        return  0;
    }

}
