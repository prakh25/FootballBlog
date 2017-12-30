package com.example.corelib.ui.authui;

import android.text.TextUtils;

import com.example.corelib.SharedPreferenceManager;
import com.example.corelib.Utils;
import com.example.corelib.model.auth.user_network.RefreshedCookie;
import com.example.corelib.model.auth.user_network.UserData;
import com.example.corelib.model.splash.notification.CallBackDevice;
import com.example.corelib.model.splash.notification.DeviceInfo;
import com.example.corelib.network.DataManager;
import com.example.corelib.network.RemoteCallback;
import com.example.corelib.ui.BasePresenter;

import java.util.UUID;

/**
 * Created by prakh on 29-12-2017.
 */

public class HomeActivityPresenter extends BasePresenter<HomeActivityContract.View>
        implements HomeActivityContract.ViewActions {

    private final DataManager dataManager;
    private final SharedPreferenceManager sharedPreferencesManager;

    public HomeActivityPresenter(DataManager dataManager,
                                  SharedPreferenceManager sharedPreferencesManager) {
        this.dataManager = dataManager;
        this.sharedPreferencesManager = sharedPreferencesManager;
    }

    @Override
    public void checkLaunch() {
        checkForLaunch();
    }

    @Override
    public void getCurrentUser() {
        if(TextUtils.isEmpty(sharedPreferencesManager.getCookie())
                || sharedPreferencesManager.getCookie() == null) {
            mView.currentUserNotFound();
            return;
        }
        validateCookie();
    }

    @Override
    public void registerDeviceFcm(String token) {
        registerDevice(token);
    }

    @Override
    public void enableNotifications() {
        setNotifications();
    }

    private void checkForLaunch() {
        if (!sharedPreferencesManager.isFirstLaunch()) {
            mView.notFirstLaunch();
            return;
        }
        sharedPreferencesManager.setFirstLaunch();
        mView.firstLaunch();
    }

    private void validateCookie() {
        dataManager.getValidatedUser(sharedPreferencesManager.getCookie(),
                new RemoteCallback<RefreshedCookie>() {
                    @Override
                    public void onSuccess(RefreshedCookie response) {
                        if(response.getUserData() == null) {
                            mView.currentUserNotFound();
                            return;
                        }
                        showUserData(response.getUserData());
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        mView.currentUserNotFound();
                    }
                });
    }

    private void showUserData(UserData userData) {

        String displayName = userData.getDisplayname();
        Integer userId = userData.getId();

        String avatarUrl = "";

        if(!TextUtils.isEmpty(userData.getWslCurrentUserImage())) {
            avatarUrl = userData.getWslCurrentUserImage();
        } else {
            avatarUrl = userData.getAvatar();
        }

        mView.currentUserValidated(displayName, avatarUrl, userId);
    }

    private void registerDevice(String token) {
        DeviceInfo deviceInfo = new DeviceInfo();

        String serial = UUID.randomUUID().toString();
        String deviceName = Utils.getDeviceName();
        String osVersion = Utils.getAndroidVersion();

        deviceInfo.regid = token;
        deviceInfo.serial = serial;
        deviceInfo.device_name = deviceName;
        deviceInfo.os_version = osVersion;

        dataManager.registerForFcm(deviceInfo, new RemoteCallback<CallBackDevice>() {
            @Override
            public void onSuccess(CallBackDevice response) {
                mView.deviceRegistered();
            }

            @Override
            public void onFailed(Throwable throwable) {
                mView.deviceRegistrationFailed();
            }
        });
    }

    private void setNotifications() {
        sharedPreferencesManager.setNotifications();
    }
}
