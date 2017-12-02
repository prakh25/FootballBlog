package com.example.corelib.model.tags_list;

import com.google.gson.annotations.Expose;

/**
 * Created by prakh on 20-11-2017.
 */

public class CategoriesOrTag {
    @Expose
    private Integer id;
    @Expose
    private Integer count;
    @Expose
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
