package com.example.prakh.footballblog.search;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by prakh on 26-12-2017.
 */

public class RecentSuggestionsProvider extends SearchRecentSuggestionsProvider {
    public static final String AUTHORITY = "com.example.prakh.footballblog.search.RecentSuggestionsProvider";
    public static final int MODE = DATABASE_MODE_QUERIES;

    public RecentSuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
