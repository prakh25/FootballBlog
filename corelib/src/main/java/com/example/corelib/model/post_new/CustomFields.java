
package com.example.corelib.model.post_new;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomFields implements Parcelable
{

    @SerializedName("wpb_post_views_count")
    @Expose
    private List<String> wpbPostViewsCount = null;
    public final static Parcelable.Creator<CustomFields> CREATOR = new Creator<CustomFields>() {


        @SuppressWarnings({
            "unchecked"
        })
        public CustomFields createFromParcel(Parcel in) {
            return new CustomFields(in);
        }

        public CustomFields[] newArray(int size) {
            return (new CustomFields[size]);
        }

    }
    ;

    protected CustomFields(Parcel in) {
        in.readList(this.wpbPostViewsCount, (java.lang.String.class.getClassLoader()));
    }

    public CustomFields() {
    }

    public List<String> getWpbPostViewsCount() {
        return wpbPostViewsCount;
    }

    public void setWpbPostViewsCount(List<String> wpbPostViewsCount) {
        this.wpbPostViewsCount = wpbPostViewsCount;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(wpbPostViewsCount);
    }

    public int describeContents() {
        return  0;
    }

}
