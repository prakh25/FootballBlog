package com.example.prakh.footballblog.interests;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.prakh.footballblog.utils.SmartFragmentStatePagerAdapter;

/**
 * Created by prakh on 20-11-2017.
 */

public class FragmentInterestViewPagerAdapter extends SmartFragmentStatePagerAdapter {

    private static final int PAGE_COUNT = 2;
    private static final String tabTitles[] = new String[] {"Categories", "Tags"};

    public FragmentInterestViewPagerAdapter(FragmentManager fragmentManager) {
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
                return new CategoriesListFragment();
            case 1:
                return new TagsListFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
