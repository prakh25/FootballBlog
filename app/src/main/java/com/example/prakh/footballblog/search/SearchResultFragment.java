package com.example.prakh.footballblog.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.corelib.SharedPreferenceManager;
import com.example.prakh.footballblog.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by prakh on 20-11-2017.
 */

public class SearchResultFragment extends Fragment {

    public static final String ARGS_QUERY = "query";

    @BindView(R.id.search_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.search_view_pager)
    ViewPager viewPager;

    private Unbinder unbinder;
    private AppCompatActivity activity;
    private String query;

    public static SearchResultFragment newInstance(String query) {

        Bundle args = new Bundle();
        args.putString(ARGS_QUERY, query);
        SearchResultFragment fragment = new SearchResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            query = getArguments().getString(ARGS_QUERY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        unbinder = ButterKnife.bind(this, view);
        activity = (AppCompatActivity) getActivity();

        if(activity != null) {
            viewPager.setAdapter(new SearchViewPagerAdapter(
                    activity.getSupportFragmentManager()));
        }
        viewPager.setOffscreenPageLimit(3);

        tabLayout.setupWithViewPager(viewPager);

        SharedPreferenceManager sharedPreference = new SharedPreferenceManager();
        sharedPreference.setQueryString(query);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}