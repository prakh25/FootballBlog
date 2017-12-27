package com.example.prakh.footballblog.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.prakh.footballblog.utils.SmartFragmentStatePagerAdapter;

/**
 * Created by prakh on 20-11-2017.
 */

public class SearchViewPagerAdapter extends SmartFragmentStatePagerAdapter {

    private static final int PAGE_COUNT = 3;
    private static final String tabTitles[] = new String[]{"Posts", "Categories", "Tags"};

    private final Bundle queryBundle;

    public SearchViewPagerAdapter(FragmentManager fragmentManager,
                                  Bundle query) {
        super(fragmentManager);
        queryBundle = query;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                SearchPostsFragment postsFragment = new SearchPostsFragment();
                postsFragment.setArguments(queryBundle);
                return postsFragment;
            case 1:
                SearchCategoriesFragment categoriesFragment = new SearchCategoriesFragment();
                categoriesFragment.setArguments(queryBundle);
                return categoriesFragment;
            case 2:
                SearchTagsFragment tagsFragment = new SearchTagsFragment();
                tagsFragment.setArguments(queryBundle);
                return tagsFragment;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
