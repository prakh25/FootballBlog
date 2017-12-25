package com.example.corelib.ui;

import android.support.annotation.NonNull;

import com.example.corelib.model.post.Post;
import com.example.corelib.model.tags_list.CategoriesOrTag;
import com.example.corelib.network.DataManager;
import com.example.corelib.network.RemoteCallback;
import com.example.corelib.realm.RealmManager;

import java.util.List;

/**
 * Created by prakh on 21-11-2017.
 */

public class SearchPresenter extends BasePresenter<SearchContract.SearchView>
        implements SearchContract.ViewActions {

    private static final String INCLUDE_FIELDS_TAGS = "id,count,name";
    private static final String INCLUDE_FIELDS_POSTS = "id,date_gmt,link,title.rendered,content.rendered,comment_status,_embedded.author,_embedded.wp:featuredmedia.first.id,_embedded.wp:featuredmedia.first.source_url,_embedded.wp:term";

    private final DataManager dataManager;
    private final RealmManager realmManager;

    public SearchPresenter(@NonNull DataManager dataManager,
                           RealmManager realmManager) {
        this.dataManager = dataManager;
        this.realmManager = realmManager;
    }

    @Override
    public void onPostQuery(String query) {
        searchPosts(query);
    }

    @Override
    public void onCategoryQuery(String query) {
        searchCategories(query);
    }

    @Override
    public void onTagsQuery(String query) {
        searchTags(query);
    }

    private void searchPosts(String query) {
        if (!isViewAttached()) return;
        mView.showMessageLayout(false);
        mView.showProgress();

        dataManager.getPostFromSearch(INCLUDE_FIELDS_POSTS, query,
                new RemoteCallback<List<Post>>() {
                    @Override
                    public void onSuccess(List<Post> response) {
                        mView.hideProgress();
                        if (response.isEmpty()) {
                            mView.showEmpty();
                            return;
                        }
                        mView.showSearchPost(response);
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        if (!isViewAttached()) return;
                        mView.hideProgress();
                        mView.showError(throwable.getMessage());
                    }
                });

//        dataManager.getPostsFromSearch(query, new RemoteCallback<PostListResponse>() {
//            @Override
//            public void onSuccess(PostListResponse response) {
//                List<Post> postList = response.getPosts();
//                if(postList.isEmpty()) {
//                    mView.hideProgress();
//                    mView.showEmpty();
//                    return;
//                }
//                mView.showSearchPost(postList);
//            }
//
//            @Override
//            public void onFailed(Throwable throwable) {
//
//            }
//        });
    }

    private void searchCategories(String query) {
        if (!isViewAttached()) return;
        mView.showMessageLayout(false);
        mView.showProgress();

        dataManager.getCategoriesFromSearch(INCLUDE_FIELDS_TAGS, query,
                new RemoteCallback<List<CategoriesOrTag>>() {
                    @Override
                    public void onSuccess(List<CategoriesOrTag> response) {
                        mView.hideProgress();
                        if (response.isEmpty()) {
                            mView.showEmpty();
                            return;
                        }
                        mView.showSearchCategories(response);
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        if (!isViewAttached()) return;
                        mView.hideProgress();
                        mView.showError(throwable.getMessage());
                    }
                });
    }

    private void searchTags(String query) {
        if (!isViewAttached()) return;
        mView.showMessageLayout(false);
        mView.showProgress();

        dataManager.getTagsFromSearch(INCLUDE_FIELDS_TAGS, query,
                new RemoteCallback<List<CategoriesOrTag>>() {
                    @Override
                    public void onSuccess(List<CategoriesOrTag> response) {
                        mView.hideProgress();
                        if (response.isEmpty()) {
                            mView.showEmpty();
                            return;
                        }
                        mView.showSearchTags(response);
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        if (!isViewAttached()) return;
                        mView.hideProgress();
                        mView.showError(throwable.getMessage());
                    }
                });
    }
}
