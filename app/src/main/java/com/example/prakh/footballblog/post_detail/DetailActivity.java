package com.example.prakh.footballblog.post_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.corelib.model.post_new.Post;
import com.example.prakh.footballblog.BaseActivity;
import com.example.prakh.footballblog.R;

/**
 * Created by prakh on 17-11-2017.
 */

public class DetailActivity extends BaseActivity {

    public static final String EXTRA_POST_ID = "extraPostId";
    public static final String EXTRA_POST = "extra_post";
    public static final String EXTRA_FROM_NOTIFICATIONS = "extra_from_notification";

    public static Intent newIntent(Context context, Post post) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_POST, post);
        return intent;
    }

    public static Intent newNotificationIntent(Context context, Integer postId) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_POST_ID, postId);
        intent.putExtra(EXTRA_FROM_NOTIFICATIONS, true);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Integer id = getIntent().getIntExtra(EXTRA_POST_ID, 0);
        Post post = getIntent().getParcelableExtra(EXTRA_POST);
        Boolean fromNotif = getIntent().getBooleanExtra(EXTRA_FROM_NOTIFICATIONS, false);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, DetailFragment.newInstance(id, post, fromNotif))
                    .commit();
        }
    }
}
