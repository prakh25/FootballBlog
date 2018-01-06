package com.example.prakh.footballblog.search;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.speech.RecognizerIntent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prakh.footballblog.BaseActivity;
import com.example.prakh.footballblog.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by prakh on 21-11-2017.
 */
public class SearchActivity extends BaseActivity implements
        SearchSuggestionAdapter.SuggestionInteractionListener {

    public static final String EXTRA_CIRCULAR_REVEAL_X = "extra_reveal_x";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "extra_reveal_y";
    public static final String EXTRA_FINAL_RADIUS = "extra_radius";

    public static final String EXTRA_RESULT_QUERY = "extra_result_query";

    public static final String EXTRA_CLICKED_SEARCH_ITEM = "clicked_search_item";

    public static final int VOICE_RECOGNITION_CODE = 100;

    @BindView(R.id.search_back_button)
    ImageView backButton;
    @BindView(R.id.search_edit_text)
    EditText searchQueryView;
    @BindView(R.id.search_voice_btn)
    ImageView voiceInputBtn;
    @BindView(R.id.search_suggestion_list)
    RecyclerView recyclerView;

    private Unbinder unbinder;
    private View rootLayout;

    private int revealX;
    private int revealY;
    private int finalRadius;

    private String query;

    public static Intent searchIntent(Context context, String query) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_RESULT_QUERY, query);
        return intent;
    }

    public static Intent newStartIntent(Context context, int revealX, int revealY, int radius) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(EXTRA_CIRCULAR_REVEAL_Y, revealY);
        intent.putExtra(EXTRA_FINAL_RADIUS, radius);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final Intent intent = getIntent();

        query = intent.getStringExtra(EXTRA_RESULT_QUERY);

        rootLayout = findViewById(R.id.search_root_view);

        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {

            rootLayout.setVisibility(View.INVISIBLE);
            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);
            finalRadius = intent.getIntExtra(EXTRA_FINAL_RADIUS, 0);

            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        revealActivity(revealX, revealY, finalRadius);
                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        } else {
            rootLayout.setVisibility(View.VISIBLE);
        }

        init();
    }

    private void init() {

        unbinder = ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        if(query == null || query.isEmpty() || query.equals("")) {
            query = "";
        } else {
            searchQueryView.setText(query);
        }

        voiceInputBtn.setSelected(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        new MapResultsFromRecentProviderList(SearchActivity.this).execute();

        initializeSearchTextListener();
        initializeDismissListener();
        initializeVoiceInputListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case VOICE_RECOGNITION_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    searchQueryView.setText(text.get(0));
                    query = text.get(0);
                    new MapResultsFromRecentProviderList(SearchActivity.this).execute();
                }
                break;
            }
        }
    }

    private void initializeSearchTextListener() {

        TextView.OnEditorActionListener searchListener = (textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                sendSearchIntent();
            }
            return true;
        };
        searchQueryView.setOnEditorActionListener(searchListener);
        searchQueryView.addTextChangedListener(textWatcher);
    }

    private void initializeDismissListener() {
        backButton.setOnClickListener(view -> onBackPressed());
    }

    private void initializeVoiceInputListener() {
        voiceInputBtn.setOnClickListener(view -> {
            if (voiceInputBtn.isSelected()) {
                searchQueryView.setText("");
                query = "";
                voiceInputBtn.setSelected(false);
                voiceInputBtn.setImageResource(R.drawable.ic_keyboard_voice_black_24dp);
            } else {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now");

                SearchActivity.this.startActivityForResult(intent, VOICE_RECOGNITION_CODE);
            }
        });
    }

    private void sendSearchIntent() {
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                RecentSuggestionsProvider.AUTHORITY, RecentSuggestionsProvider.DATABASE_MODE_QUERIES);
        suggestions.saveRecentQuery(query, null);
        startActivity(SearchResultActivity.createSearchIntent(this, query));
        finish();
    }

    private void sendSuggestionIntent(SearchSuggestionItem item) {
        try {
            Bundle bundle = new Bundle();
            bundle.putParcelable(EXTRA_CLICKED_SEARCH_ITEM, item);
            startActivity(SearchResultActivity.createSuggestionIntent(this, bundle));
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence c, int i, int i1, int i2) {
            if (!"".equals(c.toString())) {
                query = searchQueryView.getText().toString();
                setClearTextIcon();
                new MapResultsFromRecentProviderList(SearchActivity.this).execute();
            } else if (c.length() == 0) {
                setVoiceInputIcon();
                query = "";
                new MapResultsFromRecentProviderList(SearchActivity.this).execute();
            }
        }

        // Do nothing
        @Override
        public void beforeTextChanged(CharSequence c, int i, int i1, int i2) {
            // TODO: Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO: Auto-generated method stub
        }
    };

    private void setClearTextIcon() {
        voiceInputBtn.setSelected(true);
        voiceInputBtn.setImageResource(R.drawable.ic_close_black_24dp);
        voiceInputBtn.invalidate();
    }

    private void setVoiceInputIcon() {
        voiceInputBtn.setSelected(false);
        voiceInputBtn.setImageResource(R.drawable.ic_keyboard_voice_black_24dp);
        voiceInputBtn.invalidate();
    }

    private static class MapResultsFromRecentProviderList extends
            AsyncTask<Void, Void, List<SearchSuggestionItem>> {

        private WeakReference<SearchActivity> activityWeakReference;

        MapResultsFromRecentProviderList(SearchActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        private Cursor queryRecentSuggestionsProvider() {
            SearchActivity activity = activityWeakReference.get();

            Uri uri = Uri.parse("content://"
                    .concat(RecentSuggestionsProvider.AUTHORITY.concat("/suggestions")));

            String[] selection;

            selection = SearchRecentSuggestions.QUERIES_PROJECTION_1LINE;

            String[] selectionArgs = new String[]{"%" + activity.query + "%"};

            return activity.getContentResolver().query(
                    uri,
                    selection,
                    "display1 LIKE?",
                    selectionArgs,
                    "date ASC"
            );
        }

        @Override
        protected List<SearchSuggestionItem> doInBackground(Void... voids) {
            Cursor results = queryRecentSuggestionsProvider();
            List<SearchSuggestionItem> itemList = new ArrayList<>();
            Integer titleId = results.getColumnIndex("display1");
            Integer leftIconId = results.getColumnIndex(SearchManager.SUGGEST_COLUMN_ICON_1);
            Integer rightIconId = results.getColumnIndex(SearchManager.SUGGEST_COLUMN_ICON_2);

            while (results.moveToNext()) {
                String title = results.getString(titleId);
                Integer leftIcon = (leftIconId == -1) ? R.drawable.ic_restore_black_24dp : results.getInt(leftIconId);
                Integer rightIcon = (rightIconId == -1) ? R.drawable.ic_submit_arrow_24dp : results.getInt(rightIconId);

                SearchSuggestionItem aux = new SearchSuggestionItem(title, leftIcon, rightIcon);
                itemList.add(aux);
            }
            results.close();
            return itemList;
        }

        @Override
        protected void onPostExecute(List<SearchSuggestionItem> itemList) {
            SearchActivity activity = activityWeakReference.get();
            SearchSuggestionAdapter adapter = new SearchSuggestionAdapter(itemList);

            RecyclerView recyclerView = activity.findViewById(R.id.search_suggestion_list);
            recyclerView.setAdapter(adapter);
            adapter.setListener(activity);
        }
    }

    @Override
    public void onBackPressed() {
        unRevealActivity();
        super.onBackPressed();
    }

    protected void revealActivity(int x, int y, int radius) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            getWindow().setStatusBarColor(getResources().getColor(R.color.quantum_grey_600));
            // create the animator for this view (the start finalRadius is zero)
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, x, y,
                    0, (float) radius);
            circularReveal.setDuration(300);
            circularReveal.setInterpolator(new AccelerateInterpolator());

            // make the view visible and start the animation
            rootLayout.setVisibility(View.VISIBLE);
            circularReveal.start();
        } else {
            finish();
        }
    }

    protected void unRevealActivity() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            finish();
        } else {
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                    rootLayout, revealX, revealY, (float) finalRadius, 0);

            circularReveal.setDuration(300);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rootLayout.setVisibility(View.INVISIBLE);
                    finish();
                }
            });


            circularReveal.start();
        }
    }

    /**
     * Interaction listener callback for {@link SearchSuggestionAdapter}
     *
     * @param item: Recent suggestion from list od recent suggestions
     */
    @Override
    public void onSuggestionClicked(SearchSuggestionItem item) {
        sendSuggestionIntent(item);
    }

    @Override
    protected void onDestroy() {
        searchQueryView.setText(null);
        searchQueryView.removeTextChangedListener(textWatcher);
        unbinder.unbind();
        super.onDestroy();
    }
}
