package com.example.corelib.ui.authui;

import android.util.Log;

import com.example.corelib.SharedPreferenceManager;
import com.example.corelib.model.auth.UserRegisterNonce;
import com.example.corelib.model.auth.signinwithemail.GenerateAuthCookie;
import com.example.corelib.network.DataManager;
import com.example.corelib.network.RemoteCallback;
import com.example.corelib.ui.BasePresenter;

/**
 * Created by prakh on 14-12-2017.
 */

public class AuthMethodPresenter extends BasePresenter<AuthMethodContract.AuthMethodView>
        implements AuthMethodContract.ViewActions {

    private static final String INSECURE = "cool";
    private static final String CONTROLLER = "user";
    private static final String METHOD = "register";

    private final DataManager dataManager;
    private final SharedPreferenceManager sharedPreferencesManager;

    public AuthMethodPresenter(DataManager dataManager,
                                  SharedPreferenceManager sharedPreferencesManager) {
        this.dataManager = dataManager;
        this.sharedPreferencesManager = sharedPreferencesManager;
    }

    @Override
    public void getNonce(String email, String username,
                         String fistName, String lastName,
                         String displayName, String providerId, String customAvatar) {
        getRegisterNonce(email, username, fistName, lastName, displayName, providerId,
                customAvatar);
    }

    @Override
    public void loginUsingIdpProvider(String email, String username,
                                      String nonce, String fistName, String lastName,
                                      String displayName, String providerId, String customAvatar) {
        idpLogin(email, username, nonce, fistName, lastName, displayName, providerId, customAvatar);
    }

    private void idpLogin(String email, String username,
                          String nonce, String fistName, String lastName,
                          String displayName, String providerId, String customAvatar) {
        dataManager.loginUsingIdp(INSECURE, email, username, nonce, fistName, lastName, displayName,
                providerId, customAvatar, new RemoteCallback<GenerateAuthCookie>() {
                    @Override
                    public void onSuccess(GenerateAuthCookie response) {
                        if(response.getStatus().equalsIgnoreCase("ok")) {
                            sharedPreferencesManager.setCookie(response.getCookie());
                            mView.onSignInSuccessful(response.getUser());
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {

                    }
                });
    }

    private void getRegisterNonce(String email, String username,
                                  String fistName, String lastName,
                                  String displayName, String providerId, String customAvatar) {

        dataManager.getRegisterNonce(CONTROLLER, METHOD, new RemoteCallback<UserRegisterNonce>() {
            @Override
            public void onSuccess(UserRegisterNonce response) {
                if (response.getStatus().equalsIgnoreCase("ok")) {
                    Log.d("RegisterEmail","nonce:" + response.getNonce());
                    mView.nonceAcquired(response.getNonce(),email, username, fistName, lastName, displayName, providerId, customAvatar);
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                Log.d("RegisterEmail_2","" + throwable.getMessage());
            }
        });
    }


}
