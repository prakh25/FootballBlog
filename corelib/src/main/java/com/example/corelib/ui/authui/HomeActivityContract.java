package com.example.corelib.ui.authui;

/**
 * Created by prakh on 29-12-2017.
 */

public interface HomeActivityContract {

    interface ViewActions {
        void checkLaunch();

        void getCurrentUser();

        void registerDeviceFcm(String token);

        void enableNotifications();
    }

    interface View {
        void firstLaunch();

        void notFirstLaunch();

        void currentUserNotFound();

        void currentUserValidated(String displayName, String avatarUrl, Integer userId);

        void deviceRegistered();

        void deviceRegistrationFailed();

    }
}
