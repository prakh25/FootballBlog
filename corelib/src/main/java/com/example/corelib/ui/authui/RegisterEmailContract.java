package com.example.corelib.ui.authui;

/**
 * Created by prakh on 03-12-2017.
 */

public interface RegisterEmailContract {

    interface ViewActions {
        void onIntialisedRequest();

        void checkUsername(String username);

        void registerNewUser(String username, String email,
                             String password, String displayName,
                             String fistName, String lastName, String providerId);
    }

    interface RegisterEmailView {
        void isUsernameExists(boolean usernameExists);
    }
}
