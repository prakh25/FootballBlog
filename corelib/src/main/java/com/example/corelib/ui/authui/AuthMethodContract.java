package com.example.corelib.ui.authui;

import com.example.corelib.model.auth.user_network.UserData;

/**
 * Created by prakh on 05-12-2017.
 */

public interface AuthMethodContract {
    interface ViewActions {

        void getNonce(String email, String username,
                      String fistName, String lastName,
                      String displayName, String providerId, String customAvatar);

        void loginUsingIdpProvider(String email, String username, String nonce,
                                   String fistName, String lastName,
                                   String displayName, String providerId, String customAvatar);
    }

    interface AuthMethodView {
        void nonceAcquired(String nonce, String email, String username,
                           String fistName, String lastName,
                           String displayName, String providerId, String customAvatar);

        void onSignInSuccessful(UserData userInfo);
    }
}
