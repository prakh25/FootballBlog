package com.example.corelib;

import android.content.SharedPreferences;
import android.text.TextUtils;

public class SharedPreferenceManager {

    private static SharedPreferenceManager instance;

    private SharedPreferences customPreferences;
    private SharedPreferences defaultPreferences;

    public static SharedPreferenceManager getInstance() {
        if (instance == null) {
            instance = new SharedPreferenceManager();
        }
        return instance;
    }

    public SharedPreferenceManager() {
        customPreferences = MyBlogApplication.getCustomPreferences();
        defaultPreferences = MyBlogApplication.getDefaultPreference();
    }

    public void setRefreshedToken(String fcmRegId) {
        customPreferences.edit().putString("FCM_REFRESHED_PREF_KEY", fcmRegId).apply();
    }

    public String getRefreshedToken(){
        return customPreferences.getString("FCM_REFRESHED_PREF_KEY", null);
    }

    public boolean isFcmRegIdEmpty(){
        return TextUtils.isEmpty(getRefreshedToken());
    }

    public boolean getNotifications() {
        return defaultPreferences.getBoolean(str(R.string.pref_title_new_notification), true);
    }

    public String getRingtone(){
        return defaultPreferences.getString(str(R.string.pref_title_ringtone), "content://settings/system/notification_sound");
    }

    public boolean getVibration(){
        return defaultPreferences.getBoolean(str(R.string.pref_title_vibrate), true);
    }

    private String str(int string_id) {
        return MyBlogApplication.getApp().getString(string_id);
    }

    public void setQueryString(String query) {
        customPreferences.edit().putString("SEARCH_QUERY", query).apply();
    }

    public String getQueryString() {
        return customPreferences.getString("SEARCH_QUERY", null);
    }

    public void setFirstLaunch() {
        customPreferences.edit().putBoolean("FIRST_LAUNCH", true).apply();
    }

    public boolean isSecondLaunch() {
        return customPreferences.getBoolean("FIRST_LAUNCH", false);
    }

    public void setCookie(String cookie) {
        customPreferences.edit().putString("AUTH_COOKIE", cookie).apply();
    }

    public void getCookie() {
        customPreferences.getString("AUTH_COOKIE", null);
    }
}