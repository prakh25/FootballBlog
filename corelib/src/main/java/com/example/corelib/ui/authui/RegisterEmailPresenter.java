package com.example.corelib.ui.authui;

import android.util.Log;

import com.example.corelib.SharedPreferenceManager;
import com.example.corelib.model.auth.UserObject;
import com.example.corelib.model.auth.UserRegisterNonce;
import com.example.corelib.model.auth.usernamevalidator.UsernameExists;
import com.example.corelib.network.DataManager;
import com.example.corelib.network.RemoteCallback;
import com.example.corelib.ui.BasePresenter;

/**
 * Created by prakh on 03-12-2017.
 */

public class RegisterEmailPresenter extends BasePresenter<RegisterEmailContract.RegisterEmailView>
        implements RegisterEmailContract.ViewActions {

    private static final String INSECURE = "cool";
    private static final String CONTROLLER = "user";
    private static final String METHOD = "register";
    private static final Integer SECONDS = 8640000;

    private final DataManager dataManager;
    private final SharedPreferenceManager sharedPreferencesManager;

    public RegisterEmailPresenter(DataManager dataManager,
                                  SharedPreferenceManager sharedPreferencesManager) {
        this.dataManager = dataManager;
        this.sharedPreferencesManager = sharedPreferencesManager;
    }

    @Override
    public void checkUsername(String username) {
        checkIfUsernameExists(username);
    }

    @Override
    public void acquireNonce() {
        getRegisterNonce();
    }

    @Override
    public void registerNewUser(String email, String username, String nonce,
                                String password,  String fistName, String lastName,
                                String displayName, String providerId) {

        registerUserWithEmail(email, username, password, nonce, fistName, lastName,
                displayName, providerId);
    }

    private void checkIfUsernameExists(String username) {
        dataManager.ifUsernameExists(INSECURE, username, new RemoteCallback<UsernameExists>() {
            @Override
            public void onSuccess(UsernameExists response) {
                if (response.getStatus().equalsIgnoreCase("ok")) {
                    mView.isUsernameExists(response.isUsername_exists());
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                Log.d("RegisterEmail_1","" + throwable.getMessage());
            }
        });
    }

    private void getRegisterNonce() {
        dataManager.getRegisterNonce(CONTROLLER, METHOD, new RemoteCallback<UserRegisterNonce>() {
            @Override
            public void onSuccess(UserRegisterNonce response) {
                if (response.getStatus().equalsIgnoreCase("ok")) {
                    Log.d("RegisterEmail","nonce:" + response.getNonce());
                    mView.nonceAcquired(response.getNonce());
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                Log.d("RegisterEmail_2","" + throwable.getMessage());
            }
        });
    }

    private void registerUserWithEmail(String email, String username, String nonce,
                                       String password, String fistName, String lastName,
                                       String displayName, String providerId) {
        Log.d("RegisterEmail_3","nonce:" + nonce);
        dataManager.registerUserWithEmail(INSECURE, email, username, password, nonce, fistName, lastName,
                displayName, SECONDS, providerId, new RemoteCallback<UserObject>() {
                    @Override
                    public void onSuccess(UserObject response) {
                        if (response.getStatus().equalsIgnoreCase("ok")) {
                            sharedPreferencesManager.setCookie(response.getCookie());
                            mView.userRegistered(response.getUser());
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        Log.d("RegisterEmail_3","" + throwable.getMessage());
                    }
                });
    }
}
