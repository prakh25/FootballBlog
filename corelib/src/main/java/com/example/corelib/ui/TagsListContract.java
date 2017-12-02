package com.example.corelib.ui;

import com.example.corelib.model.tags_list.CategoriesOrTag;

import java.util.List;

/**
 * Created by prakh on 20-11-2017.
 */

public interface TagsListContract {
    interface ViewActions {
        void getAllTags();

        void onListEndReached(Integer pageNo);
    }

    public interface CategoriesView extends BaseView {
        void showAllTags(List<CategoriesOrTag> list);
    }
}
