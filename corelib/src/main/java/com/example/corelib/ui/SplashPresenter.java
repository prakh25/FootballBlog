package com.example.corelib.ui;

import android.provider.Settings;
import android.text.TextUtils;

import com.example.corelib.MyBlogApplication;
import com.example.corelib.SharedPreferenceManager;
import com.example.corelib.Utils;
import com.example.corelib.model.auth.UserObject;
import com.example.corelib.model.splash.ValidateCookie;
import com.example.corelib.model.splash.notification.CallBackDevice;
import com.example.corelib.model.splash.notification.DeviceInfo;
import com.example.corelib.network.DataManager;
import com.example.corelib.network.RemoteCallback;

import java.util.UUID;

/**
 * Created by prakh on 18-12-2017.
 */

public class SplashPresenter extends BasePresenter<SplashContract.SplashView>
        implements SplashContract.ViewActions {

    private final DataManager dataManager;
    private final SharedPreferenceManager sharedPreferencesManager;

    public SplashPresenter(DataManager dataManager,
                           SharedPreferenceManager sharedPreferencesManager) {
        this.dataManager = dataManager;
        this.sharedPreferencesManager = sharedPreferencesManager;
    }

    @Override
    public void validateCookie() {
        checkCookie();
    }

    @Override
    public void getCurrentUserInfo() {
        currentUser();
    }

    @Override
    public void checkForFirstLaunch() {
        checkLaunch();
    }

    @Override
    public void registerDeviceForFcm(String token) {
        registerForNotification(token);
    }

    private void checkCookie() {
        String cookie = sharedPreferencesManager.getCookie();

        dataManager.isValidCookie(cookie, new RemoteCallback<ValidateCookie>() {
            @Override
            public void onSuccess(ValidateCookie response) {
                if (!response.isValid()) {
                    mView.invalidCookie();
                    return;
                }
                mView.validCookie();
            }

            @Override
            public void onFailed(Throwable throwable) {
                mView.invalidCookie();
            }
        });
    }

    private void currentUser() {
        dataManager.getCurrentUser(sharedPreferencesManager.getCookie(),
                new RemoteCallback<UserObject>() {
                    @Override
                    public void onSuccess(UserObject response) {
                        if (response.getStatus().equalsIgnoreCase("ok")) {
                            sharedPreferencesManager.setCookie(response.getCookie());
                            mView.currentUser(response.getUser());
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {

                    }
                });
    }

    private void checkLaunch() {
        if (!sharedPreferencesManager.isFirstLaunch()) {
            mView.secondLaunch();
            return;
        }
        sharedPreferencesManager.setFirstLaunch(false);
        mView.firstLaunch();
    }

    private void registerForNotification(String token) {

        DeviceInfo deviceInfo = new DeviceInfo();
//        String token = FirebaseInstanceId.getInstance().getToken();
//        if(token == null || TextUtils.isEmpty(token)) {
//            token = sharedPreferencesManager.getRefreshedToken();
//        }
        String serial =  Settings.Secure.getString(MyBlogApplication.getApp().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if(serial == null || TextUtils.isEmpty(serial)) {
            serial = UUID.randomUUID().toString();
        }
        String deviceName = Utils.getDeviceName();
        String osVersion = Utils.getAndroidVersion();

        deviceInfo.regid = token;
        deviceInfo.serial = serial;
        deviceInfo.device_name = deviceName;
        deviceInfo.os_version = osVersion;

        dataManager.registerForFcm(deviceInfo, new RemoteCallback<CallBackDevice>() {
            @Override
            public void onSuccess(CallBackDevice response) {
                if(response.getStatus().equalsIgnoreCase("success")) {
                    mView.deviceRegistered();
                } else {
                    mView.deviceRegistrationFailed();
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                mView.deviceRegistrationFailed();
            }
        });
    }
}
