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
import com.example.corelib.ui.TagListPresenter;
import com.example.corelib.ui.TagsListContract;
import com.example.prakh.footballblog.R;
import com.example.prakh.footballblog.utils.EndlessRecyclerViewOnScrollListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by prakh on 20-11-2017.
 */

public class TagsListFragment extends Fragment implements TagsListContract.CategoriesView {

    @BindView(R.id.interest_list)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_interest)
    SwipeRefreshLayout swipeRefreshLayout;

    private Unbinder unbinder;
    private TagsListAdapter adapter;
    private TagListPresenter tagListPresenter;
    private AppCompatActivity activity;

    public TagsListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        adapter = new TagsListAdapter();
        tagListPresenter = new TagListPresenter(DataManager.getInstance());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        tagListPresenter.attachView(this);
        init(view);
        tagListPresenter.getAllTags();
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
                        tagListPresenter.onListEndReached(page);
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
        tagListPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showAllTags(List<CategoriesOrTag> list) {
        adapter.addItems(list);
    }
}
