package com.example.corelib;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by prakh on 10-11-2017.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {

    private SharedPreferenceManager sharedPreferenceManager;

    @Override
    public void onTokenRefresh() {
        sharedPreferenceManager = SharedPreferenceManager.getInstance();
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("FirebaseIDService", ""+token);
        saveRegistrationToken(token);
    }

    private void saveRegistrationToken(String token) {
        sharedPreferenceManager.setRefreshedToken(token);
    }
}
