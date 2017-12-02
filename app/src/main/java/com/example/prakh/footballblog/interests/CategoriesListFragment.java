package com.example.prakh.footballblog.interests;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.corelib.model.tags_list.CategoriesOrTag;
import com.example.corelib.network.DataManager;
import com.example.corelib.realm.RealmManager;
import com.example.corelib.ui.CategoriesListContract;
import com.example.corelib.ui.CategoryListPresenter;
import com.example.prakh.footballblog.R;
import com.example.prakh.footballblog.utils.EndlessRecyclerViewOnScrollListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by prakh on 20-11-2017.
 */

public class CategoriesListFragment extends Fragment implements CategoriesListContract.CategoriesView,
        CategoriesAdapter.CategoriesInteractionListener {

    @BindView(R.id.interest_list)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_interest)
    SwipeRefreshLayout swipeRefreshLayout;

    private Unbinder unbinder;
    private CategoriesAdapter adapter;
    private CategoryListPresenter categoryListPresenter;
    private AppCompatActivity activity;

    public CategoriesListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new CategoriesAdapter();
        categoryListPresenter = new CategoryListPresenter(DataManager.getInstance(),
                RealmManager.getInstance());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        categoryListPresenter.attachView(this);
        adapter.setListener(this);
        init(view);
        categoryListPresenter.getAllCategories();

        return view;
    }

    private void init(View view) {
        unbinder = ButterKnife.bind(this, view);
        activity = (AppCompatActivity) getActivity();

        recyclerView.setMotionEventSplittingEnabled(false);
        recyclerView.setLayoutManager(setUpLayoutManager());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        recyclerView.addOnScrollListener(setupScrollListener(recyclerView.getLayoutManager()));

        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
    }

    private RecyclerView.LayoutManager setUpLayoutManager() {
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false);
        return layoutManager;
    }

    private EndlessRecyclerViewOnScrollListener setupScrollListener(RecyclerView.LayoutManager layoutManager) {
        return new EndlessRecyclerViewOnScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                view.post(() -> {
                    if (adapter.addLoadingView()) {
                        categoryListPresenter.onListEndReached(page);
                    }
                });
            }
        };
    }

    @Override
    public void showProgress() {
        if (adapter.isEmpty()) {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void hideProgress() {
        swipeRefreshLayout.setRefreshing(false);
        adapter.removeLoadingView();
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showError(String errorMessage) {

    }

    @Override
    public void showMessageLayout(boolean show) {

    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        categoryListPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showAllCategories(List<CategoriesOrTag> list) {
        adapter.addItems(list);
    }

    @Override
    public void saveCategory(CategoriesOrTag item) {
        categoryListPresenter.onSaveCategory(item);
    }

    @Override
    public void deleteCategory(CategoriesOrTag item) {
        categoryListPresenter.onDeleteCategory(item);
    }
}
