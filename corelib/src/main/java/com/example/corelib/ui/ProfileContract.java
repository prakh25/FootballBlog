package com.example.corelib.ui;

import com.example.corelib.model.auth.user_network.UserData;
import com.example.corelib.model.post_new.Post;

/**
 * Created by prakh on 02-01-2018.
 */

public interface ProfileContract {
    interface ViewActions {
        void getCurrentUserInfo();

        void getUserPostsList();

        void getUserCommentsList();
    }

    interface ProfileView extends BaseView {
        void showCurrentUser(UserData userData);

        void showUserPostsList(Post post);

        void showUserCommentList();
    }
}
