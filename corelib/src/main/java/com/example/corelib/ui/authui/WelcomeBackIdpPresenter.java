package com.example.corelib.ui.authui;

import com.example.corelib.SharedPreferenceManager;
import com.example.corelib.model.auth.user_network.UserObject;
import com.example.corelib.network.DataManager;
import com.example.corelib.network.RemoteCallback;
import com.example.corelib.ui.BasePresenter;

/**
 * Created by prakh on 17-12-2017.
 */

public class WelcomeBackIdpPresenter extends BasePresenter<WelcomeBackIdpContract.WelcomeBackIdpView>
        implements WelcomeBackIdpContract.ViewActions {

    private static final Integer SECONDS = 8640000;

    private final DataManager dataManager;
    private final SharedPreferenceManager sharedPreferencesManager;

    public WelcomeBackIdpPresenter(DataManager dataManager,
                                     SharedPreferenceManager sharedPreferencesManager) {
        this.dataManager = dataManager;
        this.sharedPreferencesManager = sharedPreferencesManager;
    }

    @Override
    public void loginUsingIdpProvider(String email) {
        idpLogin(email);
    }

    private void idpLogin(String email) {
        dataManager.loginUserIdp(email, new RemoteCallback<UserObject>() {
                    @Override
                    public void onSuccess(UserObject response) {
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

}
