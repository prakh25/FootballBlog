package com.example.corelib.ui;

import com.example.corelib.model.post_new.Post;
import com.example.corelib.model.post_new.Tag;
import com.example.corelib.model.related_post.RelatedPost;

import java.util.List;

/**
 * Created by prakh on 12-11-2017.
 */

public interface DetailContract {

    interface ViewActions {
        void updatePostCounts(Integer postId);

        void getPostDetails(Integer postId);

        void getRelatedPosts(Integer postId);

        void getPostDetails(Post post);
    }

    interface DetailScreenView extends BaseView {

        void onPostViewsUpdated();

        void showAuthorName(String avatar, String name, String date);

        void showPostContent(Integer postId, String featureImageUrl, String postUrl,
                             String postTitle, String postContent,
                             List<Tag> tagList);

        void showRelatedPosts(List<RelatedPost> relatedPostList);
    }
}
