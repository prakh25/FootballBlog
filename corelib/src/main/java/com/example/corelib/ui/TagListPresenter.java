package com.example.corelib.ui;

import android.support.annotation.NonNull;

import com.example.corelib.model.tags_list.CategoriesOrTag;
import com.example.corelib.network.DataManager;
import com.example.corelib.network.RemoteCallback;

import java.util.List;

/**
 * Created by prakh on 20-11-2017.
 */

public class TagListPresenter extends BasePresenter<TagsListContract.CategoriesView>
        implements TagsListContract.ViewActions {

    private static final String INCLUDE_FIELDS = "id,count,name";
    private static final int INITIAL_PAGE = 1;

    private final DataManager dataManager;

    public TagListPresenter(@NonNull DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void getAllTags() {
        getTagsList(INITIAL_PAGE, INCLUDE_FIELDS);
    }

    @Override
    public void onListEndReached(Integer pageNo) {
        getTagsList(pageNo, INCLUDE_FIELDS);
    }

    private void getTagsList(Integer pageNo, String includeFields) {
        if (!isViewAttached()) return;
        mView.showMessageLayout(false);
        mView.showProgress();

        dataManager.getAllTagsList(includeFields, pageNo,
                new RemoteCallback<List<CategoriesOrTag>>() {
                    @Override
                    public void onSuccess(List<CategoriesOrTag> response) {
                        if (!isViewAttached()) return;
                        mView.hideProgress();

                        if (response.isEmpty()) {
                            mView.showEmpty();
                            return;
                        }

                        mView.showAllTags(response);
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
