package com.example.prakh.footballblog.search;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.prakh.footballblog.BaseActivity;
import com.example.prakh.footballblog.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by prakh on 21-11-2017.
 */

public class SearchActivity extends BaseActivity {

    public static final String EXTRA_CIRCULAR_REVEAL_X = "extra_reveal_x";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "extra_reveal_y";

    @BindView(R.id.search_back_button)
    ImageView backButton;
    @BindView(R.id.search_edit_text)
    EditText searchQueryView;
    @BindView(R.id.search_close_btn)
    ImageView clearButton;
    @BindView(R.id.search_voice_btn)
    ImageView voiceInputBtn;

    private Unbinder unbinder;
    private View rootLayout;

    private int revealX;
    private int revealY;

    public static Intent newStartIntent(Context context, int revealX, int revealY) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(EXTRA_CIRCULAR_REVEAL_Y, revealY);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final Intent intent = getIntent();

        rootLayout = findViewById(R.id.search_root_view);

        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {

            rootLayout.setVisibility(View.INVISIBLE);
            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);
            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        revealActivity(revealX, revealY);
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

        searchQueryView.addTextChangedListener(textWatcher);

        backButton.setOnClickListener(view -> onBackPressed());

        clearButton.setOnClickListener(view ->{
            if(getSupportFragmentManager().findFragmentById(R.id.search_container) != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(getSupportFragmentManager().findFragmentById(R.id.search_container))
                        .commit();
                searchQueryView.setText("");
                searchQueryView.requestFocus();
                showKeyboard();
            }
        });

        searchQueryView.setOnEditorActionListener((textView, i, keyEvent) -> {

            if (i == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard();
                searchAction();
                return true;
            }
            return false;
        });
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence c, int i, int i1, int i2) {
        }

        @Override
        public void beforeTextChanged(CharSequence c, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    public void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(searchQueryView, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void hideKeyboard() {
        if(TextUtils.isEmpty(searchQueryView.getText())) {
            Toast.makeText(getBaseContext(), "Please Enter String", Toast.LENGTH_SHORT).show();
            return;
        }

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private void searchAction() {
        String query = searchQueryView.getText().toString();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.search_container, SearchResultFragment.newInstance(query))
                .commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        searchQueryView.setText(null);
        searchQueryView.removeTextChangedListener(textWatcher);
        unbinder.unbind();
        super.onDestroy();
    }

    protected void revealActivity(int x, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);

            // create the animator for this view (the start radius is zero)
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, x, y, 0, finalRadius);
            circularReveal.setDuration(500);
            circularReveal.setInterpolator(new AccelerateInterpolator());

            // make the view visible and start the animation
            rootLayout.setVisibility(View.VISIBLE);
            circularReveal.start();
        } else {
            finish();
        }
    }
}
