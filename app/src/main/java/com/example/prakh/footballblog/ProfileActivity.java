package com.example.prakh.footballblog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by prakh on 02-01-2018.
 */

public class ProfileActivity extends BaseActivity {

    @BindView(R.id.profile_backdrop)
    ImageView profileBackdrop;
    @BindView(R.id.profile_image)
    CircularImageView profileImage;
    @BindView(R.id.profile_name)
    TextView profileName;
    @BindView(R.id.profile_app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.profile_collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.profile_toolbar)
    Toolbar toolbar;
    @BindView(R.id.profile_toolbar_title)
    TextView toolbarTitle;

    public static Intent createIntent(Context context) {
        return new Intent(context, ProfileActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
    }
}
