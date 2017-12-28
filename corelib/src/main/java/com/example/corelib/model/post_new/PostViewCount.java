package com.example.corelib.model.post_new;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Created by prakh on 28-12-2017.
 */

public class PostViewCount implements Parcelable {
    @Expose
    private Integer views;

    public Integer getViews() {
        return views;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.views);
    }

    public PostViewCount() {
    }

    protected PostViewCount(Parcel in) {
        this.views = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<PostViewCount> CREATOR = new Parcelable.Creator<PostViewCount>() {
        @Override
        public PostViewCount createFromParcel(Parcel source) {
            return new PostViewCount(source);
        }

        @Override
        public PostViewCount[] newArray(int size) {
            return new PostViewCount[size];
        }
    };
}
