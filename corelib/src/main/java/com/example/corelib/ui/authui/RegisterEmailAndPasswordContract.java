package com.example.corelib.ui.authui;

/**
 * Created by prakh on 03-12-2017.
 */

public interface RegisterEmailAndPasswordContract {
    interface viewActions {
        void checkEmail(String email);
    }

    interface RegisterEmailAndPasswordView {
        void isEmailPresent(boolean emailExists);
    }
}
