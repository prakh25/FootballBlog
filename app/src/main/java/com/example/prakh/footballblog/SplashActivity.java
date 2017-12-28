package com.example.prakh.footballblog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.authlib.AuthLibUi;
import com.example.authlib.IdpResponse;
import com.example.corelib.SharedPreferenceManager;
import com.example.corelib.model.auth.UserData;
import com.example.corelib.network.DataManager;
import com.example.corelib.ui.SplashContract;
import com.example.corelib.ui.SplashPresenter;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by prakh on 18-12-2017.
 */
// TODO: Remove Splash Activity in favor of splash screen and do all initial setup before starting home activity
public class SplashActivity extends BaseActivity implements
        SplashContract.SplashView {

    private static final Integer RC_SIGN_IN = 100;

    private SplashPresenter presenter;
    private SharedPreferenceManager sharedPreferenceManager;

    @BindView(R.id.root)
    View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SplashPresenter(DataManager.getInstance(),
                SharedPreferenceManager.getInstance());
        sharedPreferenceManager = SharedPreferenceManager.getInstance();
        setContentView(R.layout.activity_splash);
        presenter.attachView(this);
        findViewById(R.id.splash_progress);

        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String token ="";
        for(int i=0; i<10; i++) {
            token = FirebaseInstanceId.getInstance().getToken();
            if(!TextUtils.isEmpty(token)) break;
        }

        Log.d("SplashActivity","token: "+token);

        presenter.registerDeviceForFcm(token);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
            return;
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
    public void validCookie() {
        presenter.getCurrentUserInfo();
    }

    @Override
    public void invalidCookie() {
        presenter.checkForFirstLaunch();
    }

    @Override
    public void currentUser(UserData userData) {

    }

    @Override
    public void firstLaunch() {
        startActivityForResult(AuthLibUi.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(
                        new AuthLibUi.IdpConfig.Builder(AuthLibUi.EMAIL_PROVIDER).build(),
                        new AuthLibUi.IdpConfig.Builder(AuthLibUi.GOOGLE_PROVIDER).build(),
                        new AuthLibUi.IdpConfig.Builder(AuthLibUi.FACEBOOK_PROVIDER).build()))
                .setAllowNewEmailAccounts(true)
                .setIsSmartLockEnabled()
                .build(), RC_SIGN_IN);
    }

    @Override
    public void secondLaunch() {
        startActivity(HomeActivity.createNewIntent(this));
        finish();
    }

    @Override
    public void deviceRegistered() {
        presenter.validateCookie();
    }

    @Override
    public void deviceRegistrationFailed() {
        presenter.validateCookie();
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}
