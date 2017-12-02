package com.example.authlib.utils;

import android.support.annotation.Nullable;

import com.example.authlib.ui.FlowParameters;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by prakh on 02-12-2017.
 */

public class AuthHelper {
    private final FlowParameters mFlowParams;

    public AuthHelper(FlowParameters params) {
        mFlowParams = params;
    }

    public FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance(FirebaseApp.getInstance(mFlowParams.appName));
    }

    public CredentialsApi getCredentialsApi() {
        return Auth.CredentialsApi;
    }

    @Nullable
    public FirebaseUser getCurrentUser() {
        return getFirebaseAuth().getCurrentUser();
    }
}