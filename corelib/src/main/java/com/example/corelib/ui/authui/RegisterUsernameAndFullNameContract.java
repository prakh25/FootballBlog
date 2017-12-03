package com.example.corelib.ui.authui;

/**
 * Created by prakh on 03-12-2017.
 */

public interface RegisterUsernameAndFullNameContract {

    interface ViewActions {
        void checkUsername(String username);
    }

    interface RegisterUsernameAndFullNameView {
        void isUsernameExists(boolean usernameExists);
    }
}
