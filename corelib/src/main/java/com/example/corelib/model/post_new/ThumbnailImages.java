
package com.example.corelib.model.post_new;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ThumbnailImages implements Parcelable
{

    @SerializedName("full")
    @Expose
    private Full full;
    @SerializedName("thumbnail")
    @Expose
    private Thumbnail thumbnail;
    @SerializedName("medium")
    @Expose
    private Medium medium;
    @SerializedName("hestia-blog")
    @Expose
    private HestiaBlog hestiaBlog;
    public final static Parcelable.Creator<ThumbnailImages> CREATOR = new Creator<ThumbnailImages>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ThumbnailImages createFromParcel(Parcel in) {
            return new ThumbnailImages(in);
        }

        public ThumbnailImages[] newArray(int size) {
            return (new ThumbnailImages[size]);
        }

    }
    ;

    protected ThumbnailImages(Parcel in) {
        this.full = ((Full) in.readValue((Full.class.getClassLoader())));
        this.thumbnail = ((Thumbnail) in.readValue((Thumbnail.class.getClassLoader())));
        this.medium = ((Medium) in.readValue((Medium.class.getClassLoader())));
        this.hestiaBlog = ((HestiaBlog) in.readValue((HestiaBlog.class.getClassLoader())));
    }

    public ThumbnailImages() {
    }

    public Full getFull() {
        return full;
    }

    public void setFull(Full full) {
        this.full = full;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Medium getMedium() {
        return medium;
    }

    public void setMedium(Medium medium) {
        this.medium = medium;
    }

    public HestiaBlog getHestiaBlog() {
        return hestiaBlog;
    }

    public void setHestiaBlog(HestiaBlog hestiaBlog) {
        this.hestiaBlog = hestiaBlog;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(full);
        dest.writeValue(thumbnail);
        dest.writeValue(medium);
        dest.writeValue(hestiaBlog);
    }

    public int describeContents() {
        return  0;
    }

}
