package com.example.authlib.provider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.authlib.AuthLibUi.IdpConfig;
import com.example.authlib.IdpResponse;
import com.example.authlib.R;
import com.example.authlib.User;
import com.example.authlib.utils.GoogleApiHelper;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Created by prakh on 02-12-2017.
 */

public class GoogleProvider implements IdpProvider, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "GoogleProvider";
    private static final int RC_SIGN_IN = 20;

    private GoogleApiClient mGoogleApiClient;
    private FragmentActivity mActivity;
    private IdpConfig mIdpConfig;
    private IdpCallback mIdpCallback;
    private boolean mSpecificAccount;

    public GoogleProvider(FragmentActivity activity, IdpConfig idpConfig) {
        this(activity, idpConfig, null);
    }

    public GoogleProvider(FragmentActivity activity, IdpConfig idpConfig, @Nullable String email) {
        mActivity = activity;
        mIdpConfig = idpConfig;
        mSpecificAccount = !TextUtils.isEmpty(email);
        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .enableAutoManage(mActivity, GoogleApiHelper.getSafeAutoManageId(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, getSignInOptions(email))
                .build();
    }

    public static AuthCredential createAuthCredential(IdpResponse response) {
        return GoogleAuthProvider.getCredential(response.getIdpToken(), null);
    }

    private GoogleSignInOptions getSignInOptions(@Nullable String email) {
        String clientId = mActivity.getString(R.string.default_web_client_id);

        GoogleSignInOptions.Builder builder =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestIdToken(clientId);

        // Add additional scopes
        for (String scopeString : mIdpConfig.getScopes()) {
            builder.requestScopes(new Scope(scopeString));
        }

        if (!TextUtils.isEmpty(email)) {
            builder.setAccountName(email);
        }

        return builder.build();
    }

    @Override
    public void setAuthenticationCallback(IdpCallback callback) {
        mIdpCallback = callback;
    }

    @Override
    public String getName(Context context) {
        return "Google";
    }

    @Override
    @LayoutRes
    public int getButtonLayout() {
        return R.layout.auth_button_google_sign_in;
    }

    @Override
    public void startLogin(Activity activity) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void disconnect() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
            mGoogleApiClient = null;
        }
    }

    private IdpResponse createIdpResponse(GoogleSignInAccount account) {
        return new IdpResponse.Builder(
                new User.Builder(GoogleAuthProvider.PROVIDER_ID, account.getEmail())
                        .setName(account.getDisplayName())
                        .setPhotoUri(account.getPhotoUrl())
                        .build())
                .setToken(account.getIdToken())
                .build();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result != null) {
                if (result.isSuccess()) {
                    if (mSpecificAccount) {
                        Toast.makeText(
                                mActivity, result.getSignInAccount().getEmail(),
                                Toast.LENGTH_SHORT).show();
                    }
                    mIdpCallback.onSuccess(createIdpResponse(result.getSignInAccount()));
                } else {
                    onError(result);
                }
            } else {
                onError("No result found in intent");
            }
        }
    }

    private void onError(GoogleSignInResult result) {
        Status status = result.getStatus();

        if (status.getStatusCode() == CommonStatusCodes.INVALID_ACCOUNT) {
            mGoogleApiClient.stopAutoManage(mActivity);
            mGoogleApiClient.disconnect();
            mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                    .enableAutoManage(mActivity, GoogleApiHelper.getSafeAutoManageId(), this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, getSignInOptions(null))
                    .build();
            startLogin(mActivity);
        } else {
            if (status.getStatusCode() == CommonStatusCodes.DEVELOPER_ERROR) {
                Log.w(TAG, "Developer error: this application is misconfigured. Check your SHA1 " +
                        " and package name in the Firebase console.");
                Toast.makeText(mActivity, "Developer error.", Toast.LENGTH_SHORT).show();
            }
            onError(status.getStatusCode() + " " + status.getStatusMessage());
        }
    }

    private void onError(String errorMessage) {
        Log.e(TAG, "Error logging in with Google. " + errorMessage);
        mIdpCallback.onFailure();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(TAG, "onConnectionFailed:" + connectionResult);
    }
}
