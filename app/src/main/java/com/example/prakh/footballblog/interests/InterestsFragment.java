package com.example.prakh.footballblog.interests;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prakh.footballblog.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by prakh on 20-11-2017.
 */

public class InterestsFragment extends Fragment {

    @BindView(R.id.interest_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.fragment_interest_view_pager)
    ViewPager viewPager;

    private Unbinder unbinder;
    private AppCompatActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interests, container, false);

        init(view);
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
                    .getColor(R.color.colorPrimary)));
        }

        if(activity != null) {
            viewPager.setAdapter(new FragmentInterestViewPagerAdapter(
                    activity.getSupportFragmentManager()));
        }
        viewPager.setOffscreenPageLimit(2);

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}