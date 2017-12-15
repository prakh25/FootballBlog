package com.example.corelib.ui.authui;

import com.example.corelib.model.auth.UserData;

/**
 * Created by prakh on 04-12-2017.
 */

public interface SignInEmailContract {
    interface ViewActions {
        void loginUsingWordpress(String email, String password);
    }

    interface SignInEmailView {
        void onSignInSuccessful(UserData info);

        void invalidEmailError(String error);

        void emailDoesNotExistError(String error);
    }
}
