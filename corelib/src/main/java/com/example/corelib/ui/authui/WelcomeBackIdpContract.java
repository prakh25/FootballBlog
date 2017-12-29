package com.example.corelib.ui.authui;

import com.example.corelib.model.auth.user_network.UserData;

/**
 * Created by prakh on 17-12-2017.
 */

public interface WelcomeBackIdpContract {

    interface ViewActions {
        void loginUsingIdpProvider(String email);
    }

    interface WelcomeBackIdpView {
        void onSignInSuccessful(UserData userInfo);
    }
}
