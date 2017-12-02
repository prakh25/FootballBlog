package com.example.corelib;

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
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        sharedPreferenceManager.setRefreshedToken(token);
    }
}
