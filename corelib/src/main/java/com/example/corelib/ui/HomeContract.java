package com.example.corelib.ui;

import com.example.corelib.model.post_new.Post;

import java.util.List;

/**
 * Created by prakh on 16-11-2017.
 */

public interface HomeContract {

    interface ViewActions {

        void onRecentPostsRequested();

        void onListEndReached(int pageNo);
    }

    interface HomeScreenView extends BaseView {
        void showAllPosts(List<Post> posts);
    }
}
