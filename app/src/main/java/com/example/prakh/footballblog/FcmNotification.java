package com.example.prakh.footballblog;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by prakh on 27-12-2017.
 */

/**
 * TODO: Change notification payload
 */
public class FcmNotification implements Parcelable {

    private String title = "";
    private String content = "";
    private Integer post_id = -1;
    private String permalink = "";
    private String thumbnail = "";

    public FcmNotification() {}

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Integer getPost_id() {
        return post_id;
    }

    public String getPermalink() {
        return permalink;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeValue(this.post_id);
        dest.writeString(this.permalink);
        dest.writeString(this.thumbnail);
    }

    protected FcmNotification(Parcel in) {
        this.title = in.readString();
        this.content = in.readString();
        this.post_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.permalink = in.readString();
        this.thumbnail = in.readString();
    }

    public static final Parcelable.Creator<FcmNotification> CREATOR = new Parcelable.Creator<FcmNotification>() {
        @Override
        public FcmNotification createFromParcel(Parcel source) {
            return new FcmNotification(source);
        }

        @Override
        public FcmNotification[] newArray(int size) {
            return new FcmNotification[size];
        }
    };
}
