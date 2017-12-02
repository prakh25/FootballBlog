package com.example.corelib.ui;

import com.example.corelib.model.tags_list.CategoriesOrTag;

import java.util.List;

/**
 * Created by prakh on 20-11-2017.
 */

public interface CategoriesListContract {
    interface ViewActions {
        void getAllCategories();

        void onListEndReached(Integer pageNo);

        void onSaveCategory(CategoriesOrTag item);

        void onDeleteCategory(CategoriesOrTag item);
    }

    public interface CategoriesView extends BaseView {
        void showAllCategories(List<CategoriesOrTag> list);
    }
}
