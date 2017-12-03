package com.example.corelib.ui.authui;

/**
 * Created by prakh on 03-12-2017.
 */

public interface CheckEmailContract {
    interface viewActions {
        void checkEmail(String email);
    }

    interface CheckEmailView {
        void isEmailPresent(boolean emailExists);
    }
}
