package com.example.authlib.utils;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.CredentialsApi;

/**
 * Created by prakh on 02-12-2017.
 */

public class AuthHelper {

    public AuthHelper() {
    }

    public CredentialsApi getCredentialsApi() {
        return Auth.CredentialsApi;
    }

}