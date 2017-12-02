package com.example.prakh.footballblog.post_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.example.prakh.footballblog.BaseActivity;
import com.example.prakh.footballblog.HomeActivity;
import com.example.prakh.footballblog.R;

/**
 * Created by prakh on 17-11-2017.
 */

public class DetailActivity extends BaseActivity {

    public static final String EXTRA_POST_ID = "extraPostId";
    public static final String EXTRA_FROM_NOTIF = "extraFromNotif";

    private Boolean fromNotif;

    public static Intent createNewIntent(Context context, Integer postId, Boolean fromNotif) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_POST_ID, postId);
        intent.putExtra(EXTRA_FROM_NOTIF, fromNotif);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Integer id = getIntent().getIntExtra(EXTRA_POST_ID, 1);
        fromNotif = getIntent().getBooleanExtra(EXTRA_FROM_NOTIF, false);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, DetailFragment.newInstance(id))
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if(fromNotif) {
            startActivity(HomeActivity.createNewIntent(getApplicationContext()));
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_post_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
