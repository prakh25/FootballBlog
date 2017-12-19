package com.example.prakh.footballblog.search;

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

import com.example.corelib.SharedPreferenceManager;
import com.example.corelib.model.post.Post;
import com.example.corelib.model.tags_list.CategoriesOrTag;
import com.example.corelib.network.DataManager;
import com.example.corelib.realm.RealmManager;
import com.example.corelib.ui.SearchContract;
import com.example.corelib.ui.SearchPresenter;
import com.example.prakh.footballblog.R;
import com.example.prakh.footballblog.interests.TagsListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by prakh on 21-11-2017.
 */

public class SearchTagsFragment extends Fragment implements SearchContract.SearchView {

    public static final String ARGS_QUERY = "query";
    private static final String TAG = SearchTagsFragment.class.getSimpleName();

    @BindView(R.id.interest_list)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_interest)
    SwipeRefreshLayout swipeRefreshLayout;

    private Unbinder unbinder;
    private TagsListAdapter adapter;
    private AppCompatActivity activity;
    private SearchPresenter searchPresenter;

    private String query;
    private SharedPreferenceManager sharedPreferenceManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TagsListAdapter();
        searchPresenter = new SearchPresenter(DataManager.getInstance(),
                RealmManager.getInstance());
        sharedPreferenceManager = new SharedPreferenceManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        searchPresenter.attachView(this);
        init(view);
        query = sharedPreferenceManager.getQueryString();
        searchPresenter.onTagsQuery(query);
        return view;
    }

    private void init(View view) {
        unbinder = ButterKnife.bind(this, view);
        activity = (AppCompatActivity) getActivity();

        recyclerView.setLayoutManager(setUpLayoutManager());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
    }

    private RecyclerView.LayoutManager setUpLayoutManager() {
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false);
        return layoutManager;
    }

    @Override
    public void showProgress() {
            swipeRefreshLayout.setRefreshing(true);

    }

    @Override
    public void hideProgress() {
        swipeRefreshLayout.setRefreshing(false);
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
        searchPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showSearchPost(List<Post> searchResultList) {

    }

    @Override
    public void showSearchCategories(List<CategoriesOrTag> searchResultList) {

    }

    @Override
    public void showSearchTags(List<CategoriesOrTag> searchResultList) {
        adapter.addItems(searchResultList);
    }
}
