package com.example.prakh.footballblog.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prakh.footballblog.BaseActivity;
import com.example.prakh.footballblog.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by prakh on 26-12-2017.
 */
public class SearchResultActivity extends BaseActivity {

    public static final String ARG_SEARCH_QUERY = "arg_search_query";

    @BindView(R.id.search_result_toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_result_title)
    TextView title;
    @BindView(R.id.search_result_clear)
    ImageView clearButton;
    @BindView(R.id.search_result_back)
    ImageView backButton;
    @BindView(R.id.search_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.search_view_pager)
    ViewPager viewPager;

    private String query;

    public static Intent createSuggestionIntent(Context context, Bundle item) {
        Intent intent = new Intent(context, SearchResultActivity.class);
        intent.putExtras(item);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    public static Intent createSearchIntent(Context context, String query) {
        Intent intent = new Intent(context, SearchResultActivity.class);
        intent.putExtra(SearchManager.QUERY, query);
        intent.setAction(Intent.ACTION_SEARCH);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);
        handleIntent(getIntent());
    }

    private void initToolbar() {

        title.setText(query);

        backButton.setOnClickListener(view -> {
            onBackPressed();
            finish();
        });

        clearButton.setOnClickListener(view -> sendSearchIntent(""));

        toolbar.setOnClickListener(view -> sendSearchIntent(query));

    }

    private void sendSearchIntent(String query) {
        startActivity(SearchActivity.searchIntent(this, query));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            initToolbar();
        } else if(Intent.ACTION_VIEW.equals(intent.getAction())) {
            Bundle bundle = this.getIntent().getExtras();
            if(bundle != null) {
                SearchSuggestionItem item = bundle.getParcelable(SearchActivity.EXTRA_CLICKED_SEARCH_ITEM);
                assert item != null;
                query = item.getTitle();
            }
            initToolbar();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_SEARCH_QUERY, query);
        viewPager.setAdapter(new SearchViewPagerAdapter(getSupportFragmentManager(),
                bundle));
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
    }
}
