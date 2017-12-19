package com.example.corelib.ui;

import com.example.corelib.model.auth.UserData;

/**
 * Created by prakh on 18-12-2017.
 */

public interface SplashContract {

    interface ViewActions {
        void validateCookie();

        void getCurrentUserInfo();

        void checkForFirstLaunch();

        void registerDeviceForFcm(String token);

    }

    interface SplashView {
        void validCookie();

        void invalidCookie();

        void currentUser(UserData userData);

        void firstLaunch();

        void secondLaunch();

        void deviceRegistered();

        void deviceRegistrationFailed();
    }
}
