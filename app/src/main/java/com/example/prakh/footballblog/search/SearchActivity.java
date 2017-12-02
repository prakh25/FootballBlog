package com.example.prakh.footballblog.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
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

    @BindView(R.id.search_back_button)
    ImageView backButton;
    @BindView(R.id.search_edit_text)
    EditText searchQueryView;
    @BindView(R.id.search_close_btn)
    ImageView clearButton;
    @BindView(R.id.search_voice_btn)
    ImageView voiceInputBtn;

    private Unbinder unbinder;

    public static Intent newStartIntent(Context context) {
        return new Intent(context, SearchActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        init();

    }

    private void init() {

        unbinder = ButterKnife.bind(this);

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

    private void showKeyboard() {
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
}
