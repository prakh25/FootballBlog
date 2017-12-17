package com.example.authlib.provider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.Log;

import com.example.authlib.AuthLibUi;
import com.example.authlib.IdpResponse;
import com.example.authlib.R;
import com.example.authlib.User;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.WebDialog;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prakh on 17-12-2017.
 */

public class FacebookProvider implements IdpProvider, FacebookCallback<LoginResult> {

    private static final String TAG = "FacebookProvider";
    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";

    private static CallbackManager sCallbackManager;

    private final List<String> mScopes;
    // DO NOT USE DIRECTLY: see onSuccess(String, LoginResult) and onFailure(Bundle) below
    private IdpCallback mCallbackObject;

    public FacebookProvider(AuthLibUi.IdpConfig idpConfig, @StyleRes int theme) {
        List<String> scopes = idpConfig.getScopes();
        if (scopes == null) {
            mScopes = new ArrayList<>();
        } else {
            mScopes = scopes;
        }
        WebDialog.setWebDialogTheme(theme);
    }

    @Override
    public String getName(Context context) {
        return context.getString(R.string.auth_idp_name_facebook);
    }

    @Override
    @LayoutRes
    public int getButtonLayout() {
        return R.layout.auth_button_facebook_sign_in;
    }

    @Override
    public void startLogin(Activity activity) {
        sCallbackManager = CallbackManager.Factory.create();
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.registerCallback(sCallbackManager, this);

        List<String> permissionsList = new ArrayList<>(mScopes);

        // Ensure we have email and public_profile scopes
        if (!permissionsList.contains(EMAIL)) {
            permissionsList.add(EMAIL);
        }

        if (!permissionsList.contains(PUBLIC_PROFILE)) {
            permissionsList.add(PUBLIC_PROFILE);
        }

        // Log in with permissions
        loginManager.logInWithReadPermissions(activity, permissionsList);
    }

    @Override
    public void setAuthenticationCallback(IdpCallback callback) {
        mCallbackObject = callback;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (sCallbackManager != null) {
            sCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                (object, response) -> {
                    FacebookRequestError requestError = response.getError();
                    if (requestError != null) {
                        Log.e(TAG, "Received Facebook error: " + requestError.getErrorMessage());
                        onFailure();
                        return;
                    }
                    if (object == null) {
                        Log.w(TAG, "Received null response from Facebook GraphRequest");
                        onFailure();
                    } else {
                        String email = null;
                        String name = null;
                        Uri photoUri = null;

                        try {
                            email = object.getString("email");
                        } catch (JSONException e) {
                            Log.e(TAG, "Failure retrieving Facebook email", e);
                        }
                        try {
                            name = object.getString("name");
                        } catch (JSONException ignored) {
                        }
                        try {
                            photoUri = Uri.parse(object.getJSONObject("picture")
                                    .getJSONObject("data")
                                    .getString("url"));
                        } catch (JSONException ignored) {
                        }

                        onSuccess(loginResult, email, name, photoUri);
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture");
        request.setParameters(parameters);
        request.executeAsync();

    }

    @Override
    public void onCancel() {
        onFailure();
    }

    @Override
    public void onError(FacebookException error) {
        Log.e(TAG, "Error logging in with Facebook. " + error.getMessage());
        onFailure();
    }

    private void onSuccess(LoginResult loginResult, @Nullable String email, String name,
                           Uri photoUri) {

        gcCallbackManager();
        mCallbackObject.onSuccess(new IdpResponse.Builder(
                new User.Builder(AuthProviderId.FACEBOOK_PROVIDER_ID, email)
                        .setName(name)
                        .setPhotoUri(String.valueOf(photoUri))
                        .build())
                .setToken(loginResult.getAccessToken().getToken())
                .build());
    }

    private void onFailure() {
        gcCallbackManager();
        mCallbackObject.onFailure();
    }

    private void gcCallbackManager() {
        // sCallbackManager must be static to prevent it from being destroyed if the activity
        // containing FacebookProvider dies.
        // In startLogin(Activity), LoginManager#registerCallback(CallbackManager, FacebookCallback)
        // stores the FacebookCallback parameter--in this case a FacebookProvider instance--into
        // a HashMap in the CallbackManager instance, sCallbackManager.
        // Because FacebookProvider which contains an instance of an activity, mCallbackObject,
        // is contained in sCallbackManager, that activity will not be garbage collected.
        // Thus, we have leaked an Activity.
        sCallbackManager = null;
    }
}
