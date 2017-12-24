
package com.example.corelib.model.post_new;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Query implements Parcelable
{

    @SerializedName("ignore_sticky_posts")
    @Expose
    private Boolean ignoreStickyPosts;
    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("meta_key")
    @Expose
    private String metaKey;
    @SerializedName("orderby")
    @Expose
    private String orderby;
    public final static Parcelable.Creator<Query> CREATOR = new Creator<Query>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Query createFromParcel(Parcel in) {
            return new Query(in);
        }

        public Query[] newArray(int size) {
            return (new Query[size]);
        }

    }
    ;

    protected Query(Parcel in) {
        this.ignoreStickyPosts = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.count = ((String) in.readValue((String.class.getClassLoader())));
        this.metaKey = ((String) in.readValue((String.class.getClassLoader())));
        this.orderby = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Query() {
    }

    public Boolean getIgnoreStickyPosts() {
        return ignoreStickyPosts;
    }

    public void setIgnoreStickyPosts(Boolean ignoreStickyPosts) {
        this.ignoreStickyPosts = ignoreStickyPosts;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getMetaKey() {
        return metaKey;
    }

    public void setMetaKey(String metaKey) {
        this.metaKey = metaKey;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(ignoreStickyPosts);
        dest.writeValue(count);
        dest.writeValue(metaKey);
        dest.writeValue(orderby);
    }

    public int describeContents() {
        return  0;
    }

}
