package com.example.corelib.ui;

import android.support.annotation.NonNull;

import com.example.corelib.model.post_new.Post;
import com.example.corelib.model.post_new.PostListResponse;
import com.example.corelib.network.DataManager;
import com.example.corelib.network.RemoteCallback;
import com.example.corelib.realm.RealmManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prakh on 16-11-2017.
 */

public class HomePresenter extends BasePresenter<HomeContract.HomeScreenView>
        implements HomeContract.ViewActions {

    private final DataManager dataManager;
    private final RealmManager realmManager;

    public HomePresenter(@NonNull DataManager dataManager,
                         RealmManager realmManager) {
        this.dataManager = dataManager;
        this.realmManager = realmManager;
    }

    @Override
    public void onRecentPostsRequested() {
        getRecentPosts();
    }

    private void getRecentPosts() {
        if (!isViewAttached()) return;
        mView.showMessageLayout(false);
        mView.showProgress();

        dataManager.getRecentPosts(new RemoteCallback<PostListResponse>() {
            @Override
            public void onSuccess(PostListResponse response) {
                if (!isViewAttached()) return;
                mView.hideProgress();
                List<Post> postList = response.getPosts();

                if (postList.isEmpty()) {
                    mView.showEmpty();
                    return;
                }

                mView.showAllPosts(postList);
            }

            @Override
            public void onFailed(Throwable throwable) {

            }
        });
    }

    private List<Integer> findUserSelectedCategories() {
        List<Integer> categories = new ArrayList<>();
        categories.addAll(realmManager.getCategoriesList());
        return categories;
    }
}
