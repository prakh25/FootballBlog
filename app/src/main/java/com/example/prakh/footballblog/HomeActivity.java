package com.example.prakh.footballblog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.authlib.AuthLibUi;
import com.example.authlib.IdpResponse;
import com.example.corelib.SharedPreferenceManager;
import com.example.corelib.network.DataManager;
import com.example.corelib.ui.authui.HomeActivityContract;
import com.example.corelib.ui.authui.HomeActivityPresenter;
import com.example.prakh.footballblog.search.SearchActivity;
import com.google.firebase.iid.FirebaseInstanceId;

import java.lang.ref.WeakReference;
import java.util.Arrays;
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
    @BindView(R.id.home_root_layout)
    View rootView;

    private View navHeaderContainer;
    private ImageView authorAvatar;
    private TextView userName;
    private TextView userSeeProfile;
    private Button loginButton;

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
        displayHome();

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
        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
        }
    }

    @MainThread
    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (resultCode == RESULT_OK) {
            startActivity(HomeActivity.createNewIntent(this));
            finish();
        } else {
            if (response == null) {
                Snackbar.make(rootView, "Cancelled", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void notFirstLaunch() {
        presenter.getCurrentUser();
    }

    @Override
    public void currentUserNotFound() {

        setBackgroundToHeaderLayout();

        loginButton.setOnClickListener(view -> {
                    startActivityForResult(
                            AuthLibUi.getInstance().createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthLibUi.IdpConfig.Builder(AuthLibUi.EMAIL_PROVIDER).build(),
                                            new AuthLibUi.IdpConfig.Builder(AuthLibUi.GOOGLE_PROVIDER).build(),
                                            new AuthLibUi.IdpConfig.Builder(AuthLibUi.FACEBOOK_PROVIDER).build()))
                                    .setAllowNewEmailAccounts(true)
                                    .setIsSmartLockEnabled()
                                    .build(), RC_SIGN_IN);

                    drawerLayout.closeDrawer(GravityCompat.START);
                }
        );
    }

    @Override
    public void currentUserValidated(String displayName, String avatarUrl, Integer userId) {
        loginButton.setVisibility(View.GONE);

        userName.setVisibility(View.VISIBLE);
        userName.setText(displayName);

        userSeeProfile.setVisibility(View.VISIBLE);
        userSeeProfile.setText(R.string.nav_header_view_profile);

        authorAvatar.setVisibility(View.VISIBLE);

        navigationView.getMenu().setGroupVisible(R.id.group_3, true);

        GlideApp.with(this)
                .load(avatarUrl)
                .centerCrop()
                .circleCrop()
                .placeholder(R.drawable.profile_picture_placeholder)
                .into(authorAvatar);

        setBackgroundToHeaderLayout();
    }

    private void setBackgroundToHeaderLayout() {
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
    public void deviceRegistered() {
        Snackbar.make(rootView, "Enable Notifications", Snackbar.LENGTH_LONG)
                .setAction("ENABLE", view -> presenter.enableNotifications())
                .setActionTextColor(getResources().getColor(R.color.color_accent))
                .show();
    }

    @Override
    public void deviceRegistrationFailed() {
        Snackbar.make(rootView, "App Registration Failed", Snackbar.LENGTH_LONG)
                .setAction("RETRY", view ->
                        new RegisterDeviceForFcm(HomeActivity.this).execute())
                .setActionTextColor(getResources().getColor(R.color.color_accent))
                .show();
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
                    displayFragments(item.getItemId());
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
        loginButton = navigationView.getHeaderView(0)
                .findViewById(R.id.nav_header_button_login);

    }

    private void displayHome() {

        navigationView.setCheckedItem(R.id.nav_home);

        HomeFragment homeFragment = new HomeFragment();

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.home_container, homeFragment);
        fragmentStack.push(homeFragment);
        ft.commit();
    }

    private void displayFragments(int id) {

        Fragment fragment = null;

        switch (id) {
            case R.id.nav_home:
                fragmentStack.clear();
                displayHome();
                break;
            case R.id.nav_settings:
                startActivity(SettingsActivity.createNewIntent(this));
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(R.id.home_container, fragment);
            fragmentStack.lastElement().onPause();
            ft.hide(fragmentStack.lastElement());

            if(fragmentStack.size() >= 2) {
                ft.remove(fragmentStack.pop());
                fragmentStack.push(fragment);
            } else {
                fragmentStack.push(fragment);
            }

            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (fragmentStack.size() == 2) {
                navigationView.setCheckedItem(R.id.nav_home);
                FragmentTransaction ft = fragmentManager.beginTransaction();
                fragmentStack.lastElement().onPause();
                ft.remove(fragmentStack.pop());
                fragmentStack.lastElement().onResume();
                ft.show(fragmentStack.lastElement());
                ft.commit();
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
            while (TextUtils.isEmpty(token)) {
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
