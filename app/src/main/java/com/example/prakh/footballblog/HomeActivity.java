package com.example.prakh.footballblog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.authlib.IdpResponse;
import com.example.corelib.SharedPreferenceManager;
import com.example.corelib.network.DataManager;
import com.example.corelib.ui.authui.HomeActivityContract;
import com.example.corelib.ui.authui.HomeActivityPresenter;
import com.example.prakh.footballblog.interests.InterestsFragment;
import com.example.prakh.footballblog.search.SearchActivity;
import com.google.firebase.iid.FirebaseInstanceId;

import java.lang.ref.WeakReference;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;

// TODO: Add user profile screen and navigate to it through navigation header
// TODO: change activity transition animations
public class HomeActivity extends BaseActivity implements
        HomeActivityContract.View {

    private static final Integer RC_SIGN_IN = 100;

    @BindView(R.id.homeToolbar)
    Toolbar toolbar;
    @BindView(R.id.home_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.home_nav_view)
    NavigationView navigationView;

    private View navHeaderContainer;
    private ImageView authorAvatar;
    private TextView userName;
    private TextView userSeeProfile;
    private String toolbarTitle;

    private Stack<Fragment> fragmentStack;
    private FragmentManager fragmentManager;
    private HomeActivityPresenter presenter;

    public static Intent createNewIntent(Context context) {
        return new Intent(context, HomeActivity.class);
    }

    // TODO: Add splash screen before activity start
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new HomeActivityPresenter(DataManager.getInstance(),
                SharedPreferenceManager.getInstance());

        setContentView(R.layout.activity_home);
        presenter.attachView(this);
        fragmentStack = new Stack<>();

        ButterKnife.bind(this);

        initToolbar();
        initDrawerMenu();
        displayHome(getString(R.string.nav_home));

        presenter.checkLaunch();
    }

    @Override
    public void firstLaunch() {
        new RegisterDeviceForFcm(HomeActivity.this).execute();
        presenter.getCurrentUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
        }
    }

    @MainThread
    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if(resultCode == RESULT_OK) {
            startActivity(HomeActivity.createNewIntent(this));
            finish();
        } else {
            if(response == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                startActivity(HomeActivity.createNewIntent(this));
                finish();
            }
        }
    }

    @Override
    public void notFirstLaunch() {
        presenter.getCurrentUser();
    }

    // TODO: Add click listener to user see profile for login
    @Override
    public void currentUserNotFound() {
        GlideApp.with(this).load(R.drawable.profile_background)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource,
                                                Transition<? super Drawable> transition) {
                        navHeaderContainer.setBackground(resource);
                    }
                });
    }

    @Override
    public void currentUserValidated(String displayName, String avatarUrl, Integer userId) {
        userName.setVisibility(View.VISIBLE);
        userName.setText(displayName);
        userSeeProfile.setText("See Profile");
        GlideApp.with(this)
                .load(avatarUrl)
                .centerCrop()
                .circleCrop()
                .placeholder(R.drawable.profile_picture_placeholder)
                .into(authorAvatar);
    }

    // todo: add snack bar to show device registration for fcm with action of disabling notifications
    @Override
    public void deviceRegistered() {
        Toast.makeText(this, "Device Registered", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deviceRegistrationFailed() {
        Toast.makeText(this, "Device Registration Failed", Toast.LENGTH_SHORT).show();
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
                    displayFragments(item.getItemId(),
                            item.getTitle().toString());
                    drawerLayout.closeDrawers();
                    return true;
                }
        );

        navHeaderContainer = navigationView.getHeaderView(0)
                .findViewById(R.id.nav_header_container);
        authorAvatar = navigationView.getHeaderView(0)
                .findViewById(R.id.nav_author_avatar);
        userName = navigationView.getHeaderView(0)
                .findViewById(R.id.nav_author_name);
        userSeeProfile = navigationView.getHeaderView(0)
                .findViewById(R.id.nav_header_see_profile);

    }

    private void displayHome(String title) {

        toolbarTitle = title;

        navigationView.setCheckedItem(R.id.nav_home);

        HomeFragment homeFragment = new HomeFragment();

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.home_container, homeFragment);
        fragmentStack.push(homeFragment);
        ft.commit();
    }

    private void displayFragments(int id, String title) {

        Fragment fragment = null;

        switch (id) {
            case R.id.nav_home:
                toolbarTitle = title;
                fragmentStack.clear();
                displayHome(title);
                break;
            case R.id.nav_bookmarks:
                toolbarTitle = title;
                fragment = new HomeFragment();
                break;
            case R.id.nav_interests:
                toolbarTitle = title;
                fragment = new InterestsFragment();
                break;
            case R.id.nav_settings:
                startActivity(SettingsActivity.createNewIntent(this));
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.home_container, fragment);
            fragmentStack.push(fragment);
            ft.commit();
        }
    }

    @Override
    protected void onResume() {
        toolbar.setTitle(toolbarTitle);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (fragmentStack.size() >= 2) {
                fragmentStack.clear();
                displayHome(getString(R.string.nav_home));
            } else {
                super.onBackPressed();
            }
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

        startActivity(SearchActivity.newStartIntent(this, cx, cy, width),
                optionsCompat.toBundle());
    }

    @Override
    protected void onDestroy() {
        fragmentStack.clear();
        super.onDestroy();
    }

    private static class RegisterDeviceForFcm extends AsyncTask<Void, Void, String> {

        private WeakReference<HomeActivity> activityWeakReference;

        RegisterDeviceForFcm(HomeActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String token = "";
            while(TextUtils.isEmpty(token)) {
                token = FirebaseInstanceId.getInstance().getToken();
            }
            return token;
        }

        @Override
        protected void onPostExecute(String s) {
            HomeActivity activity = activityWeakReference.get();
            activity.presenter.registerDeviceFcm(s);
        }
    }
}
