
package com.example.corelib.model.post_new;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostListResponse implements Parcelable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("count_total")
    @Expose
    private Integer countTotal;
    @SerializedName("pages")
    @Expose
    private Integer pages;
    @SerializedName("posts")
    @Expose
    private List<Post> posts = null;
    @SerializedName("query")
    @Expose
    private Query query;
    public final static Parcelable.Creator<PostListResponse> CREATOR = new Creator<PostListResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public PostListResponse createFromParcel(Parcel in) {
            return new PostListResponse(in);
        }

        public PostListResponse[] newArray(int size) {
            return (new PostListResponse[size]);
        }

    }
    ;

    protected PostListResponse(Parcel in) {
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.count = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.countTotal = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.pages = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.posts, (com.example.corelib.model.post_new.Post.class.getClassLoader()));
        this.query = ((Query) in.readValue((Query.class.getClassLoader())));
    }

    public PostListResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getCountTotal() {
        return countTotal;
    }

    public void setCountTotal(Integer countTotal) {
        this.countTotal = countTotal;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(count);
        dest.writeValue(countTotal);
        dest.writeValue(pages);
        dest.writeList(posts);
        dest.writeValue(query);
    }

    public int describeContents() {
        return  0;
    }

}
