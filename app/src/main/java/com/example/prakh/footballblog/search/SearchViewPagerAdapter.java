package com.example.prakh.footballblog.search;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.prakh.footballblog.utils.SmartFragmentStatePagerAdapter;

/**
 * Created by prakh on 20-11-2017.
 */

public class SearchViewPagerAdapter extends SmartFragmentStatePagerAdapter {

    private static final int PAGE_COUNT = 3;
    private static final String tabTitles[] = new String[] {"Posts","Categories", "Tags"};

    public SearchViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SearchPostsFragment();
            case 1:
                return new SearchCategoriesFragment();
            case 2:
                return new SearchTagsFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
