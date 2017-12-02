package com.example.corelib.ui;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.corelib.Utils;
import com.example.corelib.model.Post;
import com.example.corelib.network.DataManager;
import com.example.corelib.network.RemoteCallback;
import com.example.corelib.realm.RealmManager;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prakh on 16-11-2017.
 */

public class HomePresenter extends BasePresenter<HomeContract.HomeScreenView>
        implements HomeContract.ViewActions {

    private static final int INITIAL_PAGE = 1;
    private static final String OS_NAME = "Android";

    private static final String INCLUDE_FIELDS = "id,date_gmt,link,title.rendered,content.rendered,comment_status,_embedded.author,_embedded.wp:featuredmedia.first.id,_embedded.wp:featuredmedia.first.source_url,_embedded.wp:term";

    private final DataManager dataManager;
    private final RealmManager realmManager;

    public HomePresenter(@NonNull DataManager dataManager,
                         RealmManager realmManager) {
        this.dataManager = dataManager;
        this.realmManager = realmManager;
    }

    @Override
    public void onIntializedRequest() {

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("Token",""+token);

        if (!realmManager.hasCategory()) {
            getAllPosts(INCLUDE_FIELDS, INITIAL_PAGE);
            return;
        }

        List<Integer> categories = new ArrayList<>();
        categories.addAll(findUserSelectedCategories());

        String includeCategories = Utils.convertListToString(categories);

        getAllPostsByUserCategories(INITIAL_PAGE, INCLUDE_FIELDS, includeCategories);
    }

    private List<Integer> findUserSelectedCategories() {
        List<Integer> categories = new ArrayList<>();
        categories.addAll(realmManager.getCategoriesList());
        return categories;
    }

    @Override
    public void onListEndReached(Integer pageNo) {
        if (!realmManager.hasCategory()) {
            getAllPosts(INCLUDE_FIELDS, pageNo);
            return;
        }

        List<Integer> categories = new ArrayList<>();
        categories.addAll(findUserSelectedCategories());

        String includeCategories = Utils.convertListToString(categories);

        getAllPostsByUserCategories(pageNo, INCLUDE_FIELDS, includeCategories);
    }

    private void getAllPosts(String includeFields, Integer pageNo) {
        if (!isViewAttached()) return;
        mView.showMessageLayout(false);
        mView.showProgress();

        dataManager.getAllPostsList(includeFields, pageNo, new RemoteCallback<List<Post>>() {
            @Override
            public void onSuccess(List<Post> response) {
                if (!isViewAttached()) return;
                mView.hideProgress();

                if (response.isEmpty()) {
                    mView.showEmpty();
                    return;
                }

                mView.showAllPosts(response);
            }

            @Override
            public void onFailed(Throwable throwable) {
                if (!isViewAttached()) return;
                mView.hideProgress();
                mView.showError(throwable.getMessage());
            }
        });
    }

    private void getAllPostsByUserCategories(Integer pageNo, String includeFields,
                                             String categories) {
        if (!isViewAttached()) return;
        mView.showMessageLayout(false);
        mView.showProgress();

        dataManager.getUserCategoriesPostList(includeFields, pageNo, categories,
                new RemoteCallback<List<Post>>() {
                    @Override
                    public void onSuccess(List<Post> response) {
                        if (!isViewAttached()) return;
                        mView.hideProgress();

                        if (response.isEmpty()) {
                            mView.showEmpty();
                            return;
                        }

                        mView.showAllPosts(response);
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
