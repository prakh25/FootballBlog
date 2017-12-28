package com.example.corelib.model.post_new;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by prakh on 28-12-2017.
 */

public class PostDetailResponse implements Parcelable {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("post")
    @Expose
    private Post post;
    @SerializedName("previous_url")
    private String previousUrl;
    @SerializedName("next_url")
    private String nextUrl;

    public String getStatus() {
        return status;
    }

    public Post getPost() {
        return post;
    }

    public String getPreviousUrl() {
        return previousUrl;
    }

    public String getNextUrl() {
        return nextUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status);
        dest.writeParcelable(this.post, flags);
        dest.writeString(this.previousUrl);
        dest.writeString(this.nextUrl);
    }

    public PostDetailResponse() {
    }

    protected PostDetailResponse(Parcel in) {
        this.status = in.readString();
        this.post = in.readParcelable(Post.class.getClassLoader());
        this.previousUrl = in.readString();
        this.nextUrl = in.readString();
    }

    public static final Parcelable.Creator<PostDetailResponse> CREATOR = new Parcelable.Creator<PostDetailResponse>() {
        @Override
        public PostDetailResponse createFromParcel(Parcel source) {
            return new PostDetailResponse(source);
        }

        @Override
        public PostDetailResponse[] newArray(int size) {
            return new PostDetailResponse[size];
        }
    };
}
