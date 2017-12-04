package com.example.corelib.ui.authui;

/**
 * Created by prakh on 03-12-2017.
 */

public interface RegisterEmailContract {

    interface ViewActions {

        void checkUsername(String username);

        void acquireNonce();

        void registerNewUser(String email, String username, String nonce,
                             String password, String fistName, String lastName,
                             String displayName, String providerId);
    }

    interface RegisterEmailView {
        void isUsernameExists(boolean usernameExists);

        void nonceAcquired(String nonce);

        void userRegistered(String status);
    }
}
