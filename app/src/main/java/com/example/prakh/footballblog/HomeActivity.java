package com.example.prakh.footballblog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.authlib.AuthLibUi;
import com.example.authlib.IdpResponse;
import com.example.corelib.SharedPreferenceManager;
import com.example.prakh.footballblog.interests.InterestsFragment;
import com.example.prakh.footballblog.search.SearchActivity;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {

    private static final Integer ANIM_DURATION = 700;
    private static final Integer RC_SIGN_IN = 100;

    @BindView(R.id.homeToolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarTitleHome)
    TextView toolbarTitle;
    @BindView(R.id.home_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.home_nav_view)
    NavigationView navigationView;

    private ImageView authorAvatar;
    private TextView userName;
    private TextView userEmail;

    private SharedPreferenceManager sharedPreferenceManager;

    public static boolean active = false;

    public static Intent createNewIntent(Context context) {
        return new Intent(context, HomeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        sharedPreferenceManager = SharedPreferenceManager.getInstance();

//        checkIsFirstLaunch();

        initToolbar();
        initDrawerMenu();

        if (savedInstanceState == null) {
            displayFragments(R.id.nav_home, getString(R.string.nav_home));
        }
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void initDrawerMenu() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(
                item -> {
                    item.setChecked(true);
                    displayFragments(item.getItemId(),
                            item.getTitle().toString());
                    drawerLayout.closeDrawers();
                    return true;
                }
        );

        authorAvatar = navigationView.getHeaderView(0)
                .findViewById(R.id.nav_author_avatar);
        userName = navigationView.getHeaderView(0)
                .findViewById(R.id.nav_author_name);
        userEmail = navigationView.getHeaderView(0)
                .findViewById(R.id.nav_author_email);
    }

    private void displayFragments(int id, String title) {
        Fragment fragment = null;
        switch (id) {
            case R.id.nav_home:
                toolbarTitle.setText(title);
                fragment = new HomeFragment();
                break;
            case R.id.nav_bookmarks:
                toolbarTitle.setText(title);
                fragment = new HomeFragment();
                break;
            case R.id.nav_interests:
                toolbarTitle.setText(title);
                fragment = new InterestsFragment();
                break;
            case R.id.nav_settings:
                startActivity(SettingsActivity.createNewIntent(this));
                break;
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.home_container, fragment)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    protected void onResume() {

        userName.setText("Prakhar Gupta");
        userEmail.setText("prakhargupta99@gmail.com");

        GlideApp.with(this)
                .load(R.mipmap.ic_launcher_round)
                .centerCrop()
                .circleCrop()
                .into(authorAvatar);

        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                presentActivity(R.id.homeToolbar);
//                startActivity(SearchActivity.newStartIntent(this));
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("PrivateResource")
    public void presentActivity(int viewID) {
        View view = findViewById(viewID);

        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                view, "transition");

        int width = view.getWidth();


        width -= (getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material)) - (getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) / 2);

        width -= getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material);

        int cx = width;
        int cy = view.getHeight() / 2;

        startActivity(SearchActivity.newStartIntent(this, cx, cy),
                optionsCompat.toBundle());
    }

    private void checkIsFirstLaunch() {
//        if (sharedPreferenceManager.isSecondLaunch()) {
//            return;
//        }

        sharedPreferenceManager.setFirstLaunch(false);
        startActivityForResult(AuthLibUi.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(
                        new AuthLibUi.IdpConfig.Builder(AuthLibUi.EMAIL_PROVIDER).build(),
                        new AuthLibUi.IdpConfig.Builder(AuthLibUi.GOOGLE_PROVIDER).build(),
                        new AuthLibUi.IdpConfig.Builder(AuthLibUi.FACEBOOK_PROVIDER).build()))
                .setAllowNewEmailAccounts(true)
                .setIsSmartLockEnabled()
                .build(), RC_SIGN_IN);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == RC_SIGN_IN) {
//            handleSignInResponse(resultCode, data);
//        }
//    }

    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);
        if (resultCode == RESULT_OK) {
            Toast.makeText(this, "Signed in as" + response.getEmail(), Toast.LENGTH_SHORT)
                    .show();
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                showSnackbar(R.string.cancelled);
                return;
            }
        }
    }

    @MainThread
    private void showSnackbar(@StringRes int errorMessageRes) {
        Toast.makeText(this, errorMessageRes, Toast.LENGTH_LONG).show();
    }
}
