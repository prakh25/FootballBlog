package com.example.corelib.ui.authui;

import com.example.corelib.model.auth.user_network.UserData;

/**
 * Created by prakh on 04-12-2017.
 */

public interface WelcomeBackEmailContract {
    interface ViewActions {
        void loginUsingWordpress(String email, String password);
    }

    interface SignInEmailView {
        void onSignInSuccessful(UserData info);

        void onSignInFailed(String error);
    }
}
