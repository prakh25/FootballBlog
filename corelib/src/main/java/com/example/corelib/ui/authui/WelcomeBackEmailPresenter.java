package com.example.corelib.ui.authui;

import com.example.corelib.SharedPreferenceManager;
import com.example.corelib.model.auth.UserObject;
import com.example.corelib.network.DataManager;
import com.example.corelib.network.RemoteCallback;
import com.example.corelib.ui.BasePresenter;

/**
 * Created by prakh on 04-12-2017.
 */

public class WelcomeBackEmailPresenter extends BasePresenter<WelcomeBackEmailContract.SignInEmailView>
        implements WelcomeBackEmailContract.ViewActions {

    private static final String INSECURE = "cool";

    private static final Integer SECONDS = 8640000;

    private final DataManager dataManager;
    private final SharedPreferenceManager sharedPreferencesManager;

    public WelcomeBackEmailPresenter(DataManager dataManager,
                                     SharedPreferenceManager sharedPreferencesManager) {
        this.dataManager = dataManager;
        this.sharedPreferencesManager = sharedPreferencesManager;
    }


    @Override
    public void loginUsingWordpress(String email, String password) {
        generateAuthCookie(email, password);
    }

    private void generateAuthCookie(String email, String password) {
        dataManager.loginUser(INSECURE, email, password, new RemoteCallback<UserObject>() {
            @Override
            public void onSuccess(UserObject response) {
                if(response.getStatus().equalsIgnoreCase("ok")) {
                    sharedPreferencesManager.setCookie(response.getCookie());
                    mView.onSignInSuccessful(response.getUser());
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                mView.onSignInFailed(throwable.getLocalizedMessage());
            }
        });
    }
}
