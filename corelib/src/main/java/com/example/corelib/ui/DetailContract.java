package com.example.corelib.ui;

import com.example.corelib.model.post.Terms;
import com.example.corelib.model.related_post.RelatedPost;

import java.util.List;

/**
 * Created by prakh on 12-11-2017.
 */

public interface DetailContract {

    interface ViewActions {
        void getPostDetails(Integer postId);
    }

    interface DetailScreenView extends BaseView {

        void showAuthorName(String avatar, String name, String date);

        void showPostContent(String featureImageUrl, String postTitle, String postContent,
                             List<Terms> tagList);

        void showRelatedPosts(List<RelatedPost> relatedPostList);
    }
}
