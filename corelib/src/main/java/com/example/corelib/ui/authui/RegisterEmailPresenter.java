package com.example.corelib.ui.authui;

import com.example.corelib.SharedPreferenceManager;
import com.example.corelib.model.auth.RegisterUserWithEmail;
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

    private String nonce;

    public RegisterEmailPresenter(DataManager dataManager,
                                  SharedPreferenceManager sharedPreferencesManager) {
        this.dataManager = dataManager;
        this.sharedPreferencesManager = sharedPreferencesManager;
    }

    @Override
    public void onIntialisedRequest() {
        getRegisterNonce();
    }

    private void getRegisterNonce() {
        dataManager.getRegisterNonce(CONTROLLER, METHOD, new RemoteCallback<UserRegisterNonce>() {
            @Override
            public void onSuccess(UserRegisterNonce response) {
                if (response.getStatus().equalsIgnoreCase("ok")) {
                    nonce = response.getNonce();
                }
            }

            @Override
            public void onFailed(Throwable throwable) {

            }
        });
    }

    @Override
    public void checkUsername(String username) {
        checkIfUsernameExists(username);
    }

    @Override
    public void registerNewUser(String username, String email,
                                String password, String displayName, String fistName,
                                String lastName, String providerId) {
        registerUserWithEmail(username, email, password, fistName,
                lastName, displayName, providerId);
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

            }
        });
    }

    private void registerUserWithEmail(String username, String email,
                                       String password, String fistName, String lastName,
                                       String displayName, String providerId) {

        dataManager.registerUser(INSECURE, email, username, nonce, password, fistName, lastName,
                displayName, SECONDS, providerId, new RemoteCallback<RegisterUserWithEmail>() {
                    @Override
                    public void onSuccess(RegisterUserWithEmail response) {
                        if (response.getStatus().equalsIgnoreCase("ok")) {
                            sharedPreferencesManager.setCookie(response.getCookie());
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {

                    }
                });
    }
}
