package com.example.corelib.ui.authui;

import android.util.Log;

import com.example.corelib.SharedPreferenceManager;
import com.example.corelib.model.auth.signinwithemail.GenerateAuthCookie;
import com.example.corelib.network.DataManager;
import com.example.corelib.network.RemoteCallback;
import com.example.corelib.ui.BasePresenter;

/**
 * Created by prakh on 04-12-2017.
 */

public class SignInEmailPresenter extends BasePresenter<SignInEmailContract.SignInEmailView>
        implements SignInEmailContract.ViewActions {

    private static final String INSECURE = "cool";

    private static final Integer SECONDS = 8640000;

    private final DataManager dataManager;
    private final SharedPreferenceManager sharedPreferencesManager;

    public SignInEmailPresenter(DataManager dataManager,
                                  SharedPreferenceManager sharedPreferencesManager) {
        this.dataManager = dataManager;
        this.sharedPreferencesManager = sharedPreferencesManager;
    }


    @Override
    public void loginUsingWordpress(String email, String password) {
        generateAuthCookie(email, password);
    }

    private void generateAuthCookie(String email, String password) {
        dataManager.loginUser(INSECURE, email, password, new RemoteCallback<GenerateAuthCookie>() {
            @Override
            public void onSuccess(GenerateAuthCookie response) {
                if(response.getStatus().equalsIgnoreCase("ok")) {
                    sharedPreferencesManager.setCookie(response.getCookie());
                    mView.onSignInSuccessful(response.getUser());
                } else if(response.getStatus().equalsIgnoreCase("error")) {
                    Log.d("SignInPresenter", "error code:" + response.getErrorId());
                    switch (response.getErrorId()) {
                        case 10:
                        case 20:
                            mView.emailDoesNotExistError(response.getError());
                            break;
                        case 30:
                            mView.invalidEmailError(response.getError());
                            break;
                    }
                }
            }

            @Override
            public void onFailed(Throwable throwable) {

            }
        });
    }
}
