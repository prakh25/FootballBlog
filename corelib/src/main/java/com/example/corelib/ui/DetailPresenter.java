package com.example.corelib.ui;

import android.support.annotation.NonNull;

import com.example.corelib.model.post_new.Post;
import com.example.corelib.model.post_new.PostDetailResponse;
import com.example.corelib.model.post_new.PostViewCount;
import com.example.corelib.model.post_new.Tag;
import com.example.corelib.model.related_post.RelatedPostsList;
import com.example.corelib.network.DataManager;
import com.example.corelib.network.RemoteCallback;

import java.util.List;

/**
 * Created by prakh on 13-11-2017.
 */

public class DetailPresenter extends BasePresenter<DetailContract.DetailScreenView>
        implements DetailContract.ViewActions {

    private final DataManager dataManager;

    public DetailPresenter(@NonNull DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void updatePostCounts(Integer postId) {
        updatePostViewCount(postId);
    }

    @Override
    public void getPostDetails(Integer postId) {
        providePostDetails(postId);
    }

    @Override
    public void getRelatedPosts(Integer postId) {
        getRelatedPost(postId);
    }

    @Override
    public void getPostDetails(Post post) {
        displayPostDetails(post);
    }

    private void updatePostViewCount(Integer postId) {
        if(!isViewAttached()) return;
        mView.showMessageLayout(false);
        mView.showProgress();

        dataManager.updatePostCount(postId, new RemoteCallback<PostViewCount>() {
            @Override
            public void onSuccess(PostViewCount response) {
                mView.onPostViewsUpdated();
            }

            @Override
            public void onFailed(Throwable throwable) {
                mView.onPostViewsUpdated();
            }
        });
    }

    private void providePostDetails(Integer postId) {

        dataManager.getPostDetails(postId, new RemoteCallback<PostDetailResponse>() {
            @Override
            public void onSuccess(PostDetailResponse response) {
                if (!isViewAttached()) return;
                if (response == null) {
                    mView.showEmpty();
                    return;
                }
                displayPostDetails(response.getPost());
            }

            @Override
            public void onFailed(Throwable throwable) {
                if (!isViewAttached()) return;
                mView.hideProgress();
                mView.showError(throwable.getMessage());
            }
        });
    }

    private void displayPostDetails(Post post) {

        Integer postId = post.getId();
        String authorName = post.getAuthor().getName();
        String postDate = post.getDate();
        String authorAvatarUrl = post.getAuthor().getAvatarUrl();

        mView.showAuthorName(authorAvatarUrl, authorName, postDate);

        String postUrl = post.getUrl();
        String postTitle = post.getTitle();

        String postFeatureImage = post.getThumbnailImages().getFull().getUrl()
                .replace("localhost", "192.168.0.23");
        String postContent = post.getContent();

        List<Tag> tagsList = post.getTags();

        mView.showPostContent(postId, postFeatureImage, postUrl,
                postTitle, postContent, tagsList);

    }

    private void getRelatedPost(Integer postId) {

        dataManager.getRelatedPosts(postId, new RemoteCallback<RelatedPostsList>() {
            @Override
            public void onSuccess(RelatedPostsList response) {
                mView.hideProgress();
                if (response.getRelatedPosts().size() > 3) {
                    mView.showRelatedPosts(response.getRelatedPosts().subList(0, 3));
                    return;
                } else if(response.getRelatedPosts().isEmpty()) {
                    return;
                }
                mView.showRelatedPosts(response.getRelatedPosts());
            }

            @Override
            public void onFailed(Throwable throwable) {
                if (!isViewAttached()) return;
                mView.hideProgress();
            }
        });
    }
}
