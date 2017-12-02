
package com.example.corelib.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Terms {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("link")
    private String link;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("slug")
    private String slug;
    @SerializedName("taxonomy")
    @Expose
    private String taxonomy;
    @SerializedName("_links")
    private TermLinks links;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
    }

    public TermLinks getLinks() {
        return links;
    }

    public void setLinks(TermLinks links) {
        this.links = links;
    }

}
