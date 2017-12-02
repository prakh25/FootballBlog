package com.example.prakh.footballblog;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.corelib.model.Post;
import com.example.corelib.network.DataManager;
import com.example.corelib.realm.RealmManager;
import com.example.corelib.ui.HomeContract;
import com.example.corelib.ui.HomePresenter;
import com.example.prakh.footballblog.post_detail.DetailActivity;
import com.example.prakh.footballblog.utils.EndlessRecyclerViewOnScrollListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by prakh on 08-11-2017.
 */

public class HomeFragment extends Fragment implements HomeContract.HomeScreenView,
        HomeAdapter.PostInteractionListener {

    @BindView(R.id.recyclerViewHome)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private Unbinder unbinder;
    private HomeAdapter adapter;
    private HomePresenter homePresenter;
    private AppCompatActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        adapter = new HomeAdapter();
        homePresenter = new HomePresenter(DataManager.getInstance(),
                RealmManager.getInstance());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        homePresenter.attachView(this);
        adapter.setListener(this);
        init(view);
        homePresenter.onIntializedRequest();

        return view;
    }

    private void init(View view) {
        unbinder = ButterKnife.bind(this, view);
        activity = (AppCompatActivity) getActivity();

        ActionBar actionBar = null;
        if(activity != null) {
            actionBar = activity.getSupportActionBar();
        }
        if(actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources()
                    .getColor(android.R.color.transparent)));
        }

        SnapHelper snapHelper = new PagerSnapHelper();
        recyclerView.setMotionEventSplittingEnabled(false);
        recyclerView.setLayoutManager(setUpLayoutManager());
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        recyclerView.addOnScrollListener(setupScrollListener(recyclerView.getLayoutManager()));
    }

    private RecyclerView.LayoutManager setUpLayoutManager() {
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(activity,
                LinearLayoutManager.HORIZONTAL, false);
        return layoutManager;
    }

    private EndlessRecyclerViewOnScrollListener setupScrollListener(RecyclerView.LayoutManager layoutManager) {
        return new EndlessRecyclerViewOnScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                view.post(() -> {
                    if (adapter.addLoadingView()) {
                        homePresenter.onListEndReached(page);
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
    public void showAllPosts(List<Post> posts) {
        adapter.addItems(posts);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        homePresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void getPostDetail(Integer postId) {
        startActivity(DetailActivity.createNewIntent(activity, postId, false));
    }
}
