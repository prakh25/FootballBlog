package com.example.prakh.footballblog.search;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.prakh.footballblog.R;

/**
 * Created by prakh on 26-12-2017.
 */

public class SearchSuggestionItem implements Parcelable {

    private String title;
    private Integer leftIcon;
    private Integer rightIcon;

    public SearchSuggestionItem() {
        title = "Error";
        leftIcon = R.drawable.ic_restore_black_24dp;
        rightIcon = R.drawable.ic_submit_arrow_24dp;
    }

    public SearchSuggestionItem(String title, Integer leftIcon, Integer rightIcon) {
        this.setTitle(title);
        this.setLeftIcon(leftIcon);
        this.setRightIcon(rightIcon);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getLeftIcon() {
        return leftIcon;
    }

    public void setLeftIcon(Integer leftIcon) {
        this.leftIcon = leftIcon;
    }

    public Integer getRightIcon() {
        return rightIcon;
    }

    public void setRightIcon(Integer rightIcon) {
        this.rightIcon = rightIcon;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeValue(this.leftIcon);
        dest.writeValue(this.rightIcon);
    }

    protected SearchSuggestionItem(Parcel in) {
        this.title = in.readString();
        this.leftIcon = (Integer) in.readValue(Integer.class.getClassLoader());
        this.rightIcon = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<SearchSuggestionItem> CREATOR = new Parcelable.Creator<SearchSuggestionItem>() {
        @Override
        public SearchSuggestionItem createFromParcel(Parcel source) {
            return new SearchSuggestionItem(source);
        }

        @Override
        public SearchSuggestionItem[] newArray(int size) {
            return new SearchSuggestionItem[size];
        }
    };
}
