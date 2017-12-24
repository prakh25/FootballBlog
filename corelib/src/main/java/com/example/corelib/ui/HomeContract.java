package com.example.corelib.ui;

import com.example.corelib.model.post.Post;

import java.util.List;

/**
 * Created by prakh on 16-11-2017.
 */

public interface HomeContract {

    interface ViewActions {

        void onIntializedRequest();
    }

    interface HomeScreenView extends BaseView {
        void showAllPosts(List<Post> posts);
    }
}
