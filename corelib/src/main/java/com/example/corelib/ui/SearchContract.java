package com.example.corelib.ui;

import com.example.corelib.model.post.Post;
import com.example.corelib.model.tags_list.CategoriesOrTag;

import java.util.List;

/**
 * Created by prakh on 21-11-2017.
 */

public interface SearchContract {

    interface ViewActions{
        void onPostQuery(String query);

        void onCategoryQuery(String query);

        void onTagsQuery(String query);
    }

    interface SearchView extends BaseView {
        void showSearchPost(List<Post> searchResultList);

        void showSearchCategories(List<CategoriesOrTag> searchResultList);

        void showSearchTags(List<CategoriesOrTag> searchResultList);
    }
}
