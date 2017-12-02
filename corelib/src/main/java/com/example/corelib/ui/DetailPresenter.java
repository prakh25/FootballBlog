package com.example.corelib.ui;

import android.support.annotation.NonNull;

import com.example.corelib.model.Post;
import com.example.corelib.model.Terms;
import com.example.corelib.model.related_post.RelatedPostsList;
import com.example.corelib.network.DataManager;
import com.example.corelib.network.RemoteCallback;

import java.util.List;

/**
 * Created by prakh on 13-11-2017.
 */

public class DetailPresenter extends BasePresenter<DetailContract.DetailScreenView>
        implements DetailContract.ViewActions {

    private static final String INCLUDE_FIELDS = "id,date_gmt,link,title.rendered,content.rendered,comment_status,_embedded.author,_embedded.replies,_embedded.wp:featuredmedia.first.id,_embedded.wp:featuredmedia.first.source_url,_embedded.wp:term";

    private final DataManager dataManager;
    private String postTitle;
    private String postUrl;

    public DetailPresenter(@NonNull DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void getPostDetails(Integer postId) {
        providePostDetails(postId, INCLUDE_FIELDS);
    }

    private void providePostDetails(Integer postId, String includeFields) {
        if (!isViewAttached()) return;
        mView.showMessageLayout(false);
        mView.showProgress();

        dataManager.getPostDetails(postId, includeFields, new RemoteCallback<Post>() {
            @Override
            public void onSuccess(Post response) {
                if (!isViewAttached()) return;

                if (response == null) {
                    mView.showEmpty();
                    return;
                }
                displayPostDetails(response);
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

        String authorName = post.getEmbedded().getAuthor().get(0).getName();
        String postDate = post.getDateGmt();
        String authorAvatarUrl = post.getEmbedded().getAuthor().get(0).getAvatarUrls().get96();

        mView.showAuthorName(authorAvatarUrl, authorName, postDate);

        postUrl = post.getLink();
        postTitle = post.getTitle().getRendered();

        String postFeatureImage = post.getEmbedded().getFeaturedMedia().getFirst().getSourceUrl()
                .replace("localhost", "192.168.0.23");
        String postContent = post.getContent().getRendered();

        List<Terms> tagsList = post.getEmbedded().getWpTerm().get(1);

        mView.showPostContent(postFeatureImage, postTitle, postContent, tagsList);

        showRelatedPost(post.getId());
    }

    private void showRelatedPost(Integer postId) {

        dataManager.getRelatedPosts(postId, new RemoteCallback<RelatedPostsList>() {
            @Override
            public void onSuccess(RelatedPostsList response) {
                mView.hideProgress();
                if (response.getRelatedPosts().size() > 3) {
                    mView.showRelatedPosts(response.getRelatedPosts().subList(0, 3));
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

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostUrl() {
        return postUrl;
    }
}
