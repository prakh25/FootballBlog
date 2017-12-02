
package com.example.corelib.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeaturedMedia {

    @SerializedName("first")
    @Expose
    private FeaturedImage first;

    public FeaturedImage getFirst() {
        return first;
    }

    public void setFirst(FeaturedImage first) {
        this.first = first;
    }

}
