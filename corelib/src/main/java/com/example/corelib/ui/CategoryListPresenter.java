package com.example.corelib.ui;

import android.support.annotation.NonNull;

import com.example.corelib.model.tags_list.CategoriesOrTag;
import com.example.corelib.network.DataManager;
import com.example.corelib.network.RemoteCallback;
import com.example.corelib.realm.RealmManager;

import java.util.List;

/**
 * Created by prakh on 20-11-2017.
 */

public class CategoryListPresenter extends BasePresenter<CategoriesListContract.CategoriesView>
        implements CategoriesListContract.ViewActions {

    private static final String INCLUDE_FIELDS = "id,count,name";
    private static final int INITIAL_PAGE = 1;

    private final DataManager dataManager;
    private final RealmManager realmManager;

    public CategoryListPresenter(@NonNull DataManager dataManager,
                                 RealmManager realmManager) {
        this.dataManager = dataManager;
        this.realmManager = realmManager;
    }

    @Override
    public void getAllCategories() {
        getCategoriesList(INITIAL_PAGE, INCLUDE_FIELDS);
    }

    @Override
    public void onListEndReached(Integer pageNo) {
        getCategoriesList(pageNo, INCLUDE_FIELDS);
    }

    @Override
    public void onSaveCategory(CategoriesOrTag item) {
        realmManager.saveCategory(item);
    }

    @Override
    public void onDeleteCategory(CategoriesOrTag item) {
        realmManager.removeCategory(item);
    }

    private void getCategoriesList(Integer pageNo, String includeFields) {
        if (!isViewAttached()) return;
        mView.showMessageLayout(false);
        mView.showProgress();

        dataManager.getAllCategoriesList(includeFields, pageNo,
                new RemoteCallback<List<CategoriesOrTag>>() {
                    @Override
                    public void onSuccess(List<CategoriesOrTag> response) {
                        if (!isViewAttached()) return;
                        mView.hideProgress();

                        if (response.isEmpty()) {
                            mView.showEmpty();
                            return;
                        }

                        mView.showAllCategories(response);
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
