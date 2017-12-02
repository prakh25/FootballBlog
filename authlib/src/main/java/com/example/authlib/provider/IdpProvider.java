package com.example.authlib.provider;

import com.example.authlib.IdpResponse;

/**
 * Created by prakh on 02-12-2017.
 */

public interface IdpProvider extends Provider {
    interface IdpCallback {
        void onSuccess(IdpResponse idpResponse);

        void onFailure();
    }

    void setAuthenticationCallback(IdpCallback callback);
}
